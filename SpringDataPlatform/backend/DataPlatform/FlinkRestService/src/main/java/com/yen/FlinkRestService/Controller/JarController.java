package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Common.ApiResponse;
import com.yen.FlinkRestService.Service.JarService;
import com.yen.FlinkRestService.Service.StorageService;
import com.yen.FlinkRestService.model.JobJar;
import com.yen.FlinkRestService.model.dto.jar.UploadJarDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/jar")
@RequiredArgsConstructor
public class JarController {

    private final JarService jarService;
    private final StorageService storageService;

    @GetMapping
    public ResponseEntity<List<JobJar>> getJobJars() {
        List<JobJar> jobJarList = jarService.getJars();
        return ResponseEntity.ok(jobJarList);
    }

    @GetMapping("/{jobJarId}")
    public ResponseEntity<JobJar> getJobJarById(@PathVariable Integer jobJarId) {
        JobJar jobJar = jarService.getJarByJobId(jobJarId);
        return ResponseEntity.ok(jobJar);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> uploadJar(@RequestParam("jarfile") MultipartFile jarFile) {
        log.info("Uploading JAR file: {}", jarFile.getOriginalFilename());

        // Store file locally
        storageService.store(jarFile);

        // Upload to Flink cluster
        UploadJarDto uploadJarDto = new UploadJarDto();
        uploadJarDto.setJarFile("upload-dir/" + jarFile.getOriginalFilename());
        jarService.addJobJar(uploadJarDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "JAR has been uploaded"));
    }

    @DeleteMapping("/{jobJarId}")
    public ResponseEntity<ApiResponse> deleteJar(@PathVariable Integer jobJarId) {
        jarService.deleteJar(jobJarId);
        return ResponseEntity.ok(new ApiResponse(true, "JAR has been deleted"));
    }

    // Legacy endpoints for backward compatibility
    @GetMapping("/")
    public ResponseEntity<List<JobJar>> getJobJarsLegacy() {
        return getJobJars();
    }

    @PostMapping("/add_jar")
    public ResponseEntity<ApiResponse> addJobJarLegacy(@RequestParam("jarfile") MultipartFile jarFile) {
        return uploadJar(jarFile);
    }
}
