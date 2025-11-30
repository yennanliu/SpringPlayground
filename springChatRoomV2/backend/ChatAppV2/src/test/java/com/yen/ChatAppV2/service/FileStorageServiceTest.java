package com.yen.ChatAppV2.service;

import com.yen.ChatAppV2.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTest {

    @TempDir
    Path tempDir;

    private FileStorageService fileStorageService;

    @BeforeEach
    void setUp() {
        fileStorageService = new FileStorageService(tempDir.toString());
    }

    @Test
    void testStoreFile() throws IOException {
        MultipartFile file = new MockMultipartFile(
            "test.txt",
            "test.txt",
            "text/plain",
            "Hello World".getBytes()
        );

        String fileName = fileStorageService.storeFile(file);

        assertNotNull(fileName);
        assertTrue(fileName.endsWith(".txt"));
        assertTrue(Files.exists(tempDir.resolve(fileName)));
    }

    @Test
    void testStoreFileWithInvalidPath() {
        MultipartFile file = new MockMultipartFile(
            "../test.txt",
            "../test.txt",
            "text/plain",
            "Hello World".getBytes()
        );

        assertThrows(RuntimeException.class, () -> fileStorageService.storeFile(file));
    }

    @Test
    void testLoadFileAsResource() throws IOException {
        // First store a file
        MultipartFile file = new MockMultipartFile(
            "test.txt",
            "test.txt",
            "text/plain",
            "Hello World".getBytes()
        );
        String fileName = fileStorageService.storeFile(file);

        // Then load it
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        assertNotNull(resource);
        assertTrue(resource.exists());
        assertEquals(fileName, resource.getFilename());
    }

    @Test
    void testLoadNonexistentFile() {
        assertThrows(NotFoundException.class,
            () -> fileStorageService.loadFileAsResource("nonexistent.txt"));
    }

    @Test
    void testStoreFilePreservesExtension() throws IOException {
        MultipartFile file = new MockMultipartFile(
            "image.jpg",
            "image.jpg",
            "image/jpeg",
            new byte[]{1, 2, 3}
        );

        String fileName = fileStorageService.storeFile(file);

        assertTrue(fileName.endsWith(".jpg"));
    }
}
