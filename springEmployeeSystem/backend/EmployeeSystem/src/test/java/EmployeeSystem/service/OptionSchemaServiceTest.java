package EmployeeSystem.service;


import EmployeeSystem.model.OptionSchema;
import EmployeeSystem.repository.OptionSchemaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


class OptionSchemaServiceTest {

    @Mock
    OptionSchemaRepository optionSchemaRepository;

    @InjectMocks
    OptionSchemaService optionSchemaService;

    /**
     * MockitoAnnotations.initMocks(this) is a method
     * from the Mockito library used to initialize
     * annotated fields for mocking.
     * When you use Mockito annotations like @Mock, @Spy, @InjectMocks, etc.,
     * you need to call this method to initialize these fields.
     *
     *
     *
     * In your test class,
     * if you have fields annotated with @Mock, @Spy, etc.,
     * and you want Mockito to manage these mocks for you,
     * you call MockitoAnnotations.initMocks(this)
     * in a method annotated with @BeforeEach or @BeforeAll
     * to initialize these mocks before each test method
     * or before all test methods, respectively.
     * This ensures that your mocks are properly set up and ready to be used in your test methods.
     *
     *
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testGetAllOptions() {
        // Mock data
        OptionSchema option1 = new OptionSchema(1, "col 1", "Option 1", true);
        OptionSchema option2 = new OptionSchema(2, "col 2", "Option 2", false);

        List<OptionSchema> mockOptions = new ArrayList<>();
        mockOptions.add(option1);
        mockOptions.add(option2);

        // Mock repository method
        when(optionSchemaRepository.findAll()).thenReturn(mockOptions);

        // Call service method
        List<OptionSchema> result = optionSchemaService.getAllOptions();

        // Verify the result
        assertEquals(2, result.size());
        assertEquals("Option 1", result.get(0).getSchemaName());
        assertEquals("Option 2", result.get(1).getSchemaName());
    }

    @Test
    public void testGetAllActiveOptions() {
        // Mock data
        OptionSchema option1 = new OptionSchema(1, "col 1", "Option 1", true);
        OptionSchema option2 = new OptionSchema(2, "col 2", "Option 2", false);

        List<OptionSchema> mockOptions = Arrays.asList(option1, option2);

        // Mock repository method
        when(optionSchemaRepository.findAll()).thenReturn(mockOptions);

        // Call service method
        List<OptionSchema> result = optionSchemaService.getAllActiveOptions();

        // Verify the result
        assertEquals(1, result.size());
        assertEquals("Option 1", result.get(0).getSchemaName());
    }

}