package com.yen.HotelApp.service.participant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.HotelApp.dto.BookingRequest;
import com.yen.HotelApp.entity.PreparedTransaction;
import com.yen.HotelApp.entity.Room;
import com.yen.HotelApp.repository.PreparedTransactionRepository;
import com.yen.HotelApp.repository.RoomRepository;
import com.yen.HotelApp.transaction.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RoomServiceParticipant implements TransactionParticipant {
    
    private static final Logger logger = LoggerFactory.getLogger(RoomServiceParticipant.class);
    private static final String PARTICIPANT_ID = "ROOM_SERVICE";
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private PreparedTransactionRepository preparedTransactionRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    @Transactional
    public ParticipantState prepare(String transactionId, TransactionContext context) {
        logger.info("Preparing room reservation for transaction: {}", transactionId);
        
        try {
            BookingRequest request = (BookingRequest) context.getTransactionData();
            
            Optional<Room> roomOpt = roomRepository.findById(request.getRoomId());
            if (roomOpt.isEmpty()) {
                logger.warn("Room not found: {} for transaction: {}", request.getRoomId(), transactionId);
                return ParticipantState.ABORTED;
            }
            
            Room room = roomOpt.get();
            
            if (!room.getAvailable()) {
                logger.warn("Room not available: {} for transaction: {}", request.getRoomId(), transactionId);
                return ParticipantState.ABORTED;
            }
            
            if (room.getPreparedTransactionId() != null) {
                logger.warn("Room already reserved in another transaction: {} for transaction: {}", 
                           request.getRoomId(), transactionId);
                return ParticipantState.ABORTED;
            }
            
            room.setPreparedTransactionId(transactionId);
            room.setAvailable(false);
            roomRepository.save(room);
            
            PreparedTransaction preparedTx = new PreparedTransaction(
                transactionId,
                PARTICIPANT_ID,
                serializeRoomData(room),
                LocalDateTime.now().plusSeconds(context.getTimeoutSeconds())
            );
            preparedTransactionRepository.save(preparedTx);
            
            logger.info("Room {} prepared for transaction: {}", request.getRoomId(), transactionId);
            return ParticipantState.PREPARED;
            
        } catch (Exception e) {
            logger.error("Error preparing room for transaction: {}", transactionId, e);
            return ParticipantState.ABORTED;
        }
    }
    
    @Override
    @Transactional
    public void commit(String transactionId) {
        logger.info("Committing room reservation for transaction: {}", transactionId);
        
        try {
            Optional<PreparedTransaction> preparedTxOpt = 
                preparedTransactionRepository.findByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
            if (preparedTxOpt.isEmpty()) {
                logger.warn("No prepared transaction found for commit: {}", transactionId);
                return;
            }
            
            PreparedTransaction preparedTx = preparedTxOpt.get();
            Room roomData = deserializeRoomData(preparedTx.getPreparedData());
            
            Optional<Room> roomOpt = roomRepository.findById(roomData.getId());
            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                room.setPreparedTransactionId(null);
                roomRepository.save(room);
                
                logger.info("Room {} committed for transaction: {}", room.getId(), transactionId);
            }
            
            preparedTransactionRepository.deleteByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
        } catch (Exception e) {
            logger.error("Error committing room for transaction: {}", transactionId, e);
        }
    }
    
    @Override
    @Transactional
    public void rollback(String transactionId) {
        logger.info("Rolling back room reservation for transaction: {}", transactionId);
        
        try {
            Optional<PreparedTransaction> preparedTxOpt = 
                preparedTransactionRepository.findByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
            if (preparedTxOpt.isEmpty()) {
                logger.warn("No prepared transaction found for rollback: {}", transactionId);
                return;
            }
            
            PreparedTransaction preparedTx = preparedTxOpt.get();
            Room roomData = deserializeRoomData(preparedTx.getPreparedData());
            
            Optional<Room> roomOpt = roomRepository.findById(roomData.getId());
            if (roomOpt.isPresent()) {
                Room room = roomOpt.get();
                room.setPreparedTransactionId(null);
                room.setAvailable(true);
                roomRepository.save(room);
                
                logger.info("Room {} rolled back for transaction: {}", room.getId(), transactionId);
            }
            
            preparedTransactionRepository.deleteByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
        } catch (Exception e) {
            logger.error("Error rolling back room for transaction: {}", transactionId, e);
        }
    }
    
    @Override
    public String getParticipantId() {
        return PARTICIPANT_ID;
    }
    
    @Override
    public boolean canTimeout() {
        return true;
    }
    
    @Override
    public int getTimeoutSeconds() {
        return 300; // 5 minutes
    }
    
    private String serializeRoomData(Room room) {
        try {
            return objectMapper.writeValueAsString(room);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing room data", e);
            return null;
        }
    }
    
    private Room deserializeRoomData(String data) {
        try {
            return objectMapper.readValue(data, Room.class);
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing room data", e);
            return null;
        }
    }
}