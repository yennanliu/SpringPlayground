package com.yen.HotelApp.config;

import com.yen.HotelApp.entity.Room;
import com.yen.HotelApp.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void run(String... args) throws Exception {
        if (roomRepository.count() == 0) {
            roomRepository.save(new Room("101", "Single", 100.0, "Cozy single room with city view"));
            roomRepository.save(new Room("102", "Single", 100.0, "Comfortable single room with garden view"));
            roomRepository.save(new Room("201", "Double", 150.0, "Spacious double room with king-size bed"));
            roomRepository.save(new Room("202", "Double", 150.0, "Double room with balcony and sea view"));
            roomRepository.save(new Room("301", "Suite", 250.0, "Luxury suite with separate living area"));
            roomRepository.save(new Room("302", "Suite", 280.0, "Presidential suite with panoramic view"));
            roomRepository.save(new Room("401", "Family", 200.0, "Family room with two double beds"));
            roomRepository.save(new Room("402", "Family", 220.0, "Large family room with kitchenette"));
            
            System.out.println("Sample room data initialized successfully!");
        }
    }
}