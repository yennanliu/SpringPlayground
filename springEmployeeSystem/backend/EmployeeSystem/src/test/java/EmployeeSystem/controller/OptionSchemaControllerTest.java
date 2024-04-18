package EmployeeSystem.controller;

import EmployeeSystem.common.ApiResponse;
import EmployeeSystem.controller.OptionSchemaController;
import EmployeeSystem.model.OptionSchema;
import EmployeeSystem.service.OptionSchemaService;
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

public class OptionSchemaControllerTest {

    @Mock
    private OptionSchemaService optionSchemaService;

    @InjectMocks
    private OptionSchemaController optionSchemaController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetSchemaOptions() {
        List<OptionSchema> optionsList = new ArrayList<>();
        optionsList.add(new OptionSchema());
        when(optionSchemaService.getAllOptions()).thenReturn(optionsList);

        ResponseEntity<List<OptionSchema>> responseEntity = optionSchemaController.getSchemaOptions();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(optionsList, responseEntity.getBody());
    }

    @Test
    public void testGetActiveSchemaOptions() {
        List<OptionSchema> activeOptionsList = new ArrayList<>();
        activeOptionsList.add(new OptionSchema());
        when(optionSchemaService.getAllActiveOptions()).thenReturn(activeOptionsList);

        ResponseEntity<List<OptionSchema>> responseEntity = optionSchemaController.getActiveSchemaOptions();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(activeOptionsList, responseEntity.getBody());
    }
}
