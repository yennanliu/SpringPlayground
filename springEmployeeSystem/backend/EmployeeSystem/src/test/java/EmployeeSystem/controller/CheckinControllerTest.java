package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.model.Checkin;
import EmployeeSystem.service.CheckinService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CheckinControllerTest {

    @Mock
    private CheckinService checkinService;

    @InjectMocks
    private CheckinController checkinController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCheckin() {
        List<Checkin> checkins = new ArrayList<>();
        checkins.add(new Checkin());
        when(checkinService.getCheckIns()).thenReturn(checkins);

        ResponseEntity<List<Checkin>> responseEntity = checkinController.getCheckin();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(checkins, responseEntity.getBody());
    }

    @Test
    public void testGetCheckinByUserId() {
        int userId = 1;
        List<Checkin> checkins = new ArrayList<>();
        checkins.add(new Checkin());
        when(checkinService.getCheckinByUserId(userId)).thenReturn(checkins);

        ResponseEntity<List<Checkin>> responseEntity = checkinController.getCheckinByUserId(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(checkins, responseEntity.getBody());
    }

    @Test
    public void testAddCheckin() {
        Checkin checkin = new Checkin();
        checkin.setUserId(1);

        ResponseEntity<ApiResponse> responseEntity = checkinController.addCheckin(checkin);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(new ApiResponse(true, "Checkin has been added").getMessage(), responseEntity.getBody().getMessage());
        verify(checkinService, times(1)).addCheckin(checkin.getUserId());
    }

}
