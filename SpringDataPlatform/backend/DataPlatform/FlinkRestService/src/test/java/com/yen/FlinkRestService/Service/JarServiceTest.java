package com.yen.FlinkRestService.Service;

import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.exception.EntityNotFoundException;
import com.yen.FlinkRestService.model.JobJar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JarServiceTest {

    @Mock
    private JobJarRepository jobJarRepository;

    @Mock
    private RestTemplateService restTemplateService;

    private JarService jarService;

    @BeforeEach
    void setUp() {
        jarService = new JarService(jobJarRepository, restTemplateService);
    }

    @Test
    void testGetJars() {
        JobJar jar1 = createJobJar(1, "test1.jar", "uploaded");
        JobJar jar2 = createJobJar(2, "test2.jar", "uploaded");

        when(jobJarRepository.findAll()).thenReturn(Arrays.asList(jar1, jar2));

        List<JobJar> result = jarService.getJars();

        assertEquals(2, result.size());
        verify(jobJarRepository, times(1)).findAll();
    }

    @Test
    void testGetJarByJobId_Found() {
        JobJar jobJar = createJobJar(1, "test.jar", "uploaded");

        when(jobJarRepository.findById(1)).thenReturn(Optional.of(jobJar));

        JobJar result = jarService.getJarByJobId(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test.jar", result.getFileName());
        assertEquals("uploaded", result.getStatus());
        verify(jobJarRepository, times(1)).findById(1);
    }

    @Test
    void testGetJarByJobId_NotFound() {
        when(jobJarRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            jarService.getJarByJobId(999);
        });

        verify(jobJarRepository, times(1)).findById(999);
    }

    @Test
    void testDeleteJar_Found() {
        JobJar jobJar = createJobJar(1, "test.jar", "uploaded");

        when(jobJarRepository.findById(1)).thenReturn(Optional.of(jobJar));
        doNothing().when(jobJarRepository).delete(jobJar);

        assertDoesNotThrow(() -> jarService.deleteJar(1));

        verify(jobJarRepository, times(1)).findById(1);
        verify(jobJarRepository, times(1)).delete(jobJar);
    }

    @Test
    void testDeleteJar_NotFound() {
        when(jobJarRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            jarService.deleteJar(999);
        });

        verify(jobJarRepository, times(1)).findById(999);
        verify(jobJarRepository, never()).delete(any(JobJar.class));
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
