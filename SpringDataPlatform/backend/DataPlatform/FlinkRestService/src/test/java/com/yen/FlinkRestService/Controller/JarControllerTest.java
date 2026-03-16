package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Service.JarService;
import com.yen.FlinkRestService.Service.StorageService;
import com.yen.FlinkRestService.exception.EntityNotFoundException;
import com.yen.FlinkRestService.exception.GlobalExceptionHandler;
import com.yen.FlinkRestService.model.JobJar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class JarControllerTest {

    private MockMvc mockMvc;

    @Mock
    private JarService jarService;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private JarController jarController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(jarController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void testGetJobJars() throws Exception {
        JobJar jar1 = createJobJar(1, "test1.jar", "uploaded");
        JobJar jar2 = createJobJar(2, "test2.jar", "uploaded");
        List<JobJar> jars = Arrays.asList(jar1, jar2);

        when(jarService.getJars()).thenReturn(jars);

        mockMvc.perform(get("/jar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].fileName", is("test1.jar")))
                .andExpect(jsonPath("$[1].id", is(2)));

        verify(jarService, times(1)).getJars();
    }

    @Test
    void testGetJobJarById_Found() throws Exception {
        JobJar jar = createJobJar(1, "test.jar", "uploaded");

        when(jarService.getJarByJobId(1)).thenReturn(jar);

        mockMvc.perform(get("/jar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fileName", is("test.jar")))
                .andExpect(jsonPath("$.status", is("uploaded")));

        verify(jarService, times(1)).getJarByJobId(1);
    }

    @Test
    void testGetJobJarById_NotFound() throws Exception {
        when(jarService.getJarByJobId(999))
                .thenThrow(new EntityNotFoundException("JobJar", 999));

        mockMvc.perform(get("/jar/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));

        verify(jarService, times(1)).getJarByJobId(999);
    }

    @Test
    void testUploadJar() throws Exception {
        MockMultipartFile jarFile = new MockMultipartFile(
                "jarfile",
                "test-job.jar",
                "application/java-archive",
                "dummy jar content".getBytes()
        );

        JobJar savedJar = createJobJar(1, "test-job.jar", "uploaded");
        doNothing().when(storageService).store(any());
        when(jarService.addJobJar(any())).thenReturn(savedJar);

        mockMvc.perform(multipart("/jar").file(jarFile))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("uploaded")));

        verify(storageService, times(1)).store(any());
        verify(jarService, times(1)).addJobJar(any());
    }

    @Test
    void testDeleteJar() throws Exception {
        doNothing().when(jarService).deleteJar(1);

        mockMvc.perform(delete("/jar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("deleted")));

        verify(jarService, times(1)).deleteJar(1);
    }

    @Test
    void testDeleteJar_NotFound() throws Exception {
        doThrow(new EntityNotFoundException("JobJar", 999))
                .when(jarService).deleteJar(999);

        mockMvc.perform(delete("/jar/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success", is(false)));

        verify(jarService, times(1)).deleteJar(999);
    }

    @Test
    void testGetJobJarsLegacy() throws Exception {
        JobJar jar = createJobJar(1, "test.jar", "uploaded");
        when(jarService.getJars()).thenReturn(Arrays.asList(jar));

        mockMvc.perform(get("/jar/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(jarService, times(1)).getJars();
    }

    @Test
    void testAddJobJarLegacy() throws Exception {
        MockMultipartFile jarFile = new MockMultipartFile(
                "jarfile",
                "legacy-test.jar",
                "application/java-archive",
                "dummy jar content".getBytes()
        );

        JobJar savedJar = createJobJar(1, "legacy-test.jar", "uploaded");
        doNothing().when(storageService).store(any());
        when(jarService.addJobJar(any())).thenReturn(savedJar);

        mockMvc.perform(multipart("/jar/add_jar").file(jarFile))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)));

        verify(storageService, times(1)).store(any());
    }

    private JobJar createJobJar(Integer id, String fileName, String status) {
        JobJar jobJar = new JobJar();
        jobJar.setId(id);
        jobJar.setFileName(fileName);
        jobJar.setStatus(status);
        jobJar.setUploadTime(new Date());
        jobJar.setSavedJarName("saved-" + fileName);
        return jobJar;
    }
}
