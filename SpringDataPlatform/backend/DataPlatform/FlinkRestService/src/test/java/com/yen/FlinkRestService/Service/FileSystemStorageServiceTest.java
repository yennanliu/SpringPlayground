//package com.yen.FlinkRestService.Service;
//
//import com.yen.FlinkRestService.Config.StorageProperties;
//import com.yen.FlinkRestService.exception.StorageException;
//import com.yen.FlinkRestService.exception.StorageFileNotFoundException;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.io.TempDir;
//
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import org.springframework.core.io.Resource;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class FileSystemStorageServiceTest {
//
//    @Mock
//    private StorageProperties storageProperties;
//
//    @InjectMocks
//    private FileSystemStorageService storageService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testStoreAndLoadAll(@TempDir Path tempDir) throws IOException {
//
//        when(storageProperties.getLocation()).thenReturn(tempDir.toString());
//        storageService.init();
//
//        MultipartFile file = new MockMultipartFile("test-file", "test.txt", "text/plain", "Hello, World!".getBytes());
//        storageService.store(file);
//
//        Stream<Path> pathStream = storageService.loadAll();
//        assertTrue(pathStream.anyMatch(path -> path.toString().equals("test.txt")));
//
//        Resource resource = storageService.loadAsResource("test.txt");
//        assertNotNull(resource);
//        assertTrue(resource.exists());
//        assertTrue(resource.isReadable());
//    }
//
//    @Test
//    void testStoreEmptyFile() {
//
//        MultipartFile file = new MockMultipartFile("empty-file", "", "text/plain", new byte[0]);
//
//        StorageException exception = assertThrows(StorageException.class, () -> storageService.store(file));
//        assertEquals("Failed to store empty file.", exception.getMessage());
//    }
//
//    @Test
//    void testStoreOutsideCurrentDirectory() {
//
//        MultipartFile file = new MockMultipartFile("outside-file", "../outside.txt", "text/plain", "Outside file".getBytes());
//
//        StorageException exception = assertThrows(StorageException.class, () -> storageService.store(file));
//        assertEquals("Cannot store file outside current directory.", exception.getMessage());
//    }
//
//    @Test
//    void testLoadNonExistentFile() {
//
//        StorageFileNotFoundException exception = assertThrows(StorageFileNotFoundException.class, () -> storageService.loadAsResource("non-existent.txt"));
//        assertEquals("Could not read file: non-existent.txt", exception.getMessage());
//    }
//
//    @Test
//    void testDeleteAll(@TempDir Path tempDir) throws IOException {
//
//        when(storageProperties.getLocation()).thenReturn(tempDir.toString());
//        storageService.init();
//
//        MultipartFile file = new MockMultipartFile("test-file", "test.txt", "text/plain", "Hello, World!".getBytes());
//        storageService.store(file);
//
//        assertTrue(Files.exists(Paths.get(tempDir.toString(), "test.txt")));
//
//        storageService.deleteAll();
//
//        assertFalse(Files.exists(Paths.get(tempDir.toString(), "test.txt")));
//    }
//
//}
