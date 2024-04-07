package EmployeeSystem.service;

import EmployeeSystem.model.Checkin;
import EmployeeSystem.repository.CheckinRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckinServiceTest {

    @Mock
    CheckinRepository checkinRepository;

    @InjectMocks
    CheckinService checkinService;

    // TODO : double check
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCheckIns(){

        List<Checkin> checkins = new ArrayList<>();
        checkins.add(new Checkin(1,1, new Date()));
        checkins.add(new Checkin(2,2, new Date()));

        // mock resp response
        when(checkinRepository.findAll()).thenReturn(checkins);

        List<Checkin> result = checkinService.getCheckIns();

        assertEquals(checkins, result);
        assertEquals(checkins.size(), 2);
    }

    @Test
    public void testGetCheckInsWithNullResult(){

        List<Checkin> checkins = new ArrayList<>();

        // mock resp response
        when(checkinRepository.findAll()).thenReturn(checkins);

        List<Checkin> result = checkinService.getCheckIns();

        assertEquals(checkins, result);
        assertEquals(checkins.size(), 0);
    }

    @Test
    public void testGetCheckinByUserId(){

        List<Checkin> checkins = new ArrayList<>();
        Checkin checkin1 = new Checkin(1,1, new Date());
        Checkin checkin2 = new Checkin(2,2, new Date());
        checkins.add(checkin1);
        checkins.add(checkin2);

        // mock resp response
        when(checkinRepository.findAll()).thenReturn(checkins);

        List<Checkin> result = checkinService.getCheckinByUserId(1);
        List<Checkin> expected = checkins.stream().filter(x -> x.getUserId() == 1).collect(Collectors.toList());

        assertEquals(expected, result);
        assertEquals(expected.size(), 1);
    }

    @Test
    public void testGetCheckinByUserIdNullResult(){

        List<Checkin> checkins = new ArrayList<>();
        Checkin checkin1 = new Checkin(1,1, new Date());
        Checkin checkin2 = new Checkin(2,2, new Date());
        checkins.add(checkin1);
        checkins.add(checkin2);

        // mock resp response
        when(checkinRepository.findAll()).thenReturn(checkins);

        List<Checkin> result = checkinService.getCheckinByUserId(3);
        List<Checkin> expected = checkins.stream().filter(x -> x.getUserId() == 3).collect(Collectors.toList());

        assertEquals(expected, result);
        assertEquals(expected.size(), 0);
    }

//    @Test
//    public void testAddCheckin(){
//
//        Integer userId = 1;
//        Checkin checkin1 = new Checkin(1,userId, new Date("2024-01-01"));
//        checkinService.addCheckin(userId);
//
//        verify(checkinRepository, times(1)).save(checkin1);
//    }

}