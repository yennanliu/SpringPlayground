package com.yen.HotelApp.service.participant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yen.HotelApp.dto.BookingRequest;
import com.yen.HotelApp.entity.Booking;
import com.yen.HotelApp.entity.PreparedTransaction;
import com.yen.HotelApp.entity.Room;
import com.yen.HotelApp.repository.BookingRepository;
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
public class BookingServiceParticipant implements TransactionParticipant {
    
    private static final Logger logger = LoggerFactory.getLogger(BookingServiceParticipant.class);
    private static final String PARTICIPANT_ID = "BOOKING_SERVICE";
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private RoomRepository roomRepository;
    
    @Autowired
    private PreparedTransactionRepository preparedTransactionRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Override
    @Transactional
    public ParticipantState prepare(String transactionId, TransactionContext context) {
        logger.info("Preparing booking creation for transaction: {}", transactionId);
        
        try {
            BookingRequest request = (BookingRequest) context.getTransactionData();
            
            Optional<Room> roomOpt = roomRepository.findById(request.getRoomId());
            if (roomOpt.isEmpty()) {
                logger.warn("Room not found: {} for transaction: {}", request.getRoomId(), transactionId);
                return ParticipantState.ABORTED;
            }
            
            Room room = roomOpt.get();
            
            if (request.getCheckInDate().isAfter(request.getCheckOutDate()) || 
                request.getCheckInDate().isEqual(request.getCheckOutDate())) {
                logger.warn("Invalid date range for transaction: {}", transactionId);
                return ParticipantState.ABORTED;
            }
            
            Booking booking = new Booking();
            booking.setRoom(room);
            booking.setGuestName(request.getGuestName());
            booking.setGuestEmail(request.getGuestEmail());
            booking.setCheckInDate(request.getCheckInDate());
            booking.setCheckOutDate(request.getCheckOutDate());
            booking.setTotalPrice(request.getTotalPrice());
            booking.setStatus(Booking.BookingStatus.PREPARED);
            booking.setPreparedTransactionId(transactionId);
            
            Booking savedBooking = bookingRepository.save(booking);
            
            PreparedTransaction preparedTx = new PreparedTransaction(
                transactionId,
                PARTICIPANT_ID,
                serializeBookingData(savedBooking),
                LocalDateTime.now().plusSeconds(context.getTimeoutSeconds())
            );
            preparedTransactionRepository.save(preparedTx);
            
            logger.info("Booking prepared for transaction: {} with booking ID: {}", transactionId, savedBooking.getId());
            return ParticipantState.PREPARED;
            
        } catch (Exception e) {
            logger.error("Error preparing booking for transaction: {}", transactionId, e);
            return ParticipantState.ABORTED;
        }
    }
    
    @Override
    @Transactional
    public void commit(String transactionId) {
        logger.info("Committing booking for transaction: {}", transactionId);
        
        try {
            Optional<PreparedTransaction> preparedTxOpt = 
                preparedTransactionRepository.findByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
            if (preparedTxOpt.isEmpty()) {
                logger.warn("No prepared transaction found for commit: {}", transactionId);
                return;
            }
            
            PreparedTransaction preparedTx = preparedTxOpt.get();
            Booking bookingData = deserializeBookingData(preparedTx.getPreparedData());
            
            Optional<Booking> bookingOpt = bookingRepository.findById(bookingData.getId());
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                booking.setStatus(Booking.BookingStatus.CONFIRMED);
                booking.setPreparedTransactionId(null);
                bookingRepository.save(booking);
                
                logger.info("Booking {} committed for transaction: {}", booking.getId(), transactionId);
            }
            
            preparedTransactionRepository.deleteByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
        } catch (Exception e) {
            logger.error("Error committing booking for transaction: {}", transactionId, e);
        }
    }
    
    @Override
    @Transactional
    public void rollback(String transactionId) {
        logger.info("Rolling back booking for transaction: {}", transactionId);
        
        try {
            Optional<PreparedTransaction> preparedTxOpt = 
                preparedTransactionRepository.findByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
            if (preparedTxOpt.isEmpty()) {
                logger.warn("No prepared transaction found for rollback: {}", transactionId);
                return;
            }
            
            PreparedTransaction preparedTx = preparedTxOpt.get();
            Booking bookingData = deserializeBookingData(preparedTx.getPreparedData());
            
            Optional<Booking> bookingOpt = bookingRepository.findById(bookingData.getId());
            if (bookingOpt.isPresent()) {
                Booking booking = bookingOpt.get();
                booking.setStatus(Booking.BookingStatus.CANCELLED);
                bookingRepository.save(booking);
                
                logger.info("Booking {} rolled back for transaction: {}", booking.getId(), transactionId);
            }
            
            preparedTransactionRepository.deleteByTransactionIdAndParticipantId(transactionId, PARTICIPANT_ID);
            
        } catch (Exception e) {
            logger.error("Error rolling back booking for transaction: {}", transactionId, e);
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
    
    private String serializeBookingData(Booking booking) {
        try {
            return objectMapper.writeValueAsString(booking);
        } catch (JsonProcessingException e) {
            logger.error("Error serializing booking data", e);
            return null;
        }
    }
    
    private Booking deserializeBookingData(String data) {
        try {
            return objectMapper.readValue(data, Booking.class);
        } catch (JsonProcessingException e) {
            logger.error("Error deserializing booking data", e);
            return null;
        }
    }
}