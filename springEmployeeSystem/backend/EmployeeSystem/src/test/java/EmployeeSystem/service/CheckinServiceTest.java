package EmployeeSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import EmployeeSystem.model.Checkin;
import EmployeeSystem.repository.CheckinRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CheckinServiceTest {

  @Mock CheckinRepository checkinRepository;

  @InjectMocks CheckinService checkinService;

  // TODO : double check
  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testGetCheckIns() {

    List<Checkin> checkins = new ArrayList<>();
    checkins.add(new Checkin(1, 1, new Date()));
    checkins.add(new Checkin(2, 2, new Date()));

    // mock resp response
    when(checkinRepository.findAll()).thenReturn(checkins);

    List<Checkin> result = checkinService.getCheckIns();

    assertEquals(checkins, result);
    assertEquals(checkins.size(), 2);
  }

  @Test
  public void testGetCheckInsWithNullResult() {

    List<Checkin> checkins = new ArrayList<>();

    // mock resp response
    when(checkinRepository.findAll()).thenReturn(checkins);

    List<Checkin> result = checkinService.getCheckIns();

    assertEquals(checkins, result);
    assertEquals(checkins.size(), 0);
  }

  @Test
  public void testGetCheckinByUserId() {

    Checkin checkin1 = new Checkin(1, 1, new Date());
    List<Checkin> expected = new ArrayList<>();
    expected.add(checkin1);

    // service now calls findByUserId, not findAll
    when(checkinRepository.findByUserId(1)).thenReturn(expected);

    List<Checkin> result = checkinService.getCheckinByUserId(1);

    assertEquals(expected, result);
    assertEquals(1, result.size());
  }

  @Test
  public void testGetCheckinByUserIdNullResult() {

    when(checkinRepository.findByUserId(3)).thenReturn(new ArrayList<>());

    List<Checkin> result = checkinService.getCheckinByUserId(3);

    assertEquals(new ArrayList<>(), result);
    assertEquals(0, result.size());
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
