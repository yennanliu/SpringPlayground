package com.yen.HotelApp.repository;

import com.yen.HotelApp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByAvailable(Boolean available);
    List<Room> findByRoomType(String roomType);
    List<Room> findByAvailableAndRoomType(Boolean available, String roomType);
}