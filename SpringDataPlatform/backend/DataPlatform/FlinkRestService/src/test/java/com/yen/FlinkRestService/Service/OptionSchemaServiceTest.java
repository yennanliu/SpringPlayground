package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.OptionSchemaRepository;
import com.yen.FlinkRestService.model.OptionSchema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class OptionSchemaServiceTest {

    @InjectMocks
    private OptionSchemaService optionSchemaService;

    @Mock
    private OptionSchemaRepository optionSchemaRepository;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllOptions() {

        // Arrange
        List<OptionSchema> optionSchemas = Arrays.asList(
                new OptionSchema(1, "option1", "schema", true),
                new OptionSchema(2, "option2","schema", false)
        );
        when(optionSchemaRepository.findAll()).thenReturn(optionSchemas);

        // Act
        List<OptionSchema> result = optionSchemaService.getAllOptions();

        // Assert
        assertEquals(optionSchemas, result);
    }

    @Test
    public void testGetAllActiveOptions() {
        // Arrange
        List<OptionSchema> optionSchemas = Arrays.asList(
                new OptionSchema(1, "option1", "schema", true),
                new OptionSchema(2, "option2","schema", false),
                new OptionSchema(3, "option3", "schema",true)
        );
        when(optionSchemaRepository.findAll()).thenReturn(optionSchemas);

        // Act
        List<OptionSchema> result = optionSchemaService.getAllActiveOptions();

        // Assert
        List<OptionSchema> expectedActiveOptions = Arrays.asList(
                new OptionSchema(1, "option1", "schema",true),
                new OptionSchema(3, "option3", "schema", true)
        );
        assertEquals(expectedActiveOptions, result);
    }

}