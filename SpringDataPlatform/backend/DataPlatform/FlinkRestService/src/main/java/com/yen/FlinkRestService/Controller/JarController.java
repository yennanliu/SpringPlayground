package com.yen.FlinkRestService.Controller;

import com.yen.FlinkRestService.Common.ApiResponse;
import com.yen.FlinkRestService.Repository.JobJarRepository;
import com.yen.FlinkRestService.Service.StorageService;
import com.yen.FlinkRestService.model.JobJar;
import com.yen.FlinkRestService.model.dto.jar.UploadJarDto;
import com.yen.FlinkRestService.Service.JarService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/jar")
public class JarController {

    @Autowired
    JarService jarService;

    @Autowired
    JobJarRepository jobJarRepository;

    @Autowired
    private StorageService storageService;

    @GetMapping("/")
    public ResponseEntity<List<JobJar>> getJobJar(){

        List<JobJar> jobJarList = jarService.getJars();
        return new ResponseEntity<>(jobJarList, HttpStatus.OK);
    }

    @GetMapping("/{jobJarId}")
    public ResponseEntity<JobJar> getJobJarById(@PathVariable("jobJarId") Integer jobJarId) {

        JobJar jobJar = jarService.getJarByJobId(jobJarId);
        return new ResponseEntity<>(jobJar, HttpStatus.OK);
    }

    /**
     *  curl cmd : curl -X POST -F "jarfile=@/Users/yennanliu/flink-1.17.2/examples/table/StreamSQLExample.jar" http://localhost:8081/jars/upload
     */
    @PostMapping("/add_jar")
    public ResponseEntity<ApiResponse> addJobJar(@RequestParam("jarfile") MultipartFile jarFile){

        String fileName = jarFile.getOriginalFilename() + "-" + System.currentTimeMillis();
        log.info(">>> save file to local : " + fileName);
        storageService.store(jarFile);
        // TODO : optimize below, move save logic to service
        UploadJarDto uploadJarDto = new UploadJarDto();
        uploadJarDto.setJarFile("upload-dir/"+ jarFile.getOriginalFilename());
        jarService.addJobJar(uploadJarDto);
        return new ResponseEntity<>(new ApiResponse(true, "Job jar has been added"), HttpStatus.CREATED);
    }

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadJarFile(@RequestParam("jarfile") MultipartFile jarFile) {
//
//        System.out.println(">>> OriginalFilename: " + jarFile.getOriginalFilename());
//        // Example: Print the file name and size
//        System.out.println(">>> Uploaded JAR File Name: " + jarFile.getOriginalFilename());
//        System.out.println(">>> Uploaded JAR File Size: " + jarFile.getSize());
//
//        // save
//        // Ensure the upload directory exists
//        String uploadDirectory = "/upload-dir";
//        //Path filePath = new Path(jarFile.getOriginalFilename());
//        File directory = new File(uploadDirectory);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//        // Generate a unique filename for the uploaded JAR file
//        String fileName = jarFile.getOriginalFilename() + "-" + System.currentTimeMillis();
//        log.info(">>> save file to local : " + fileName);
//        storageService.store(jarFile);
//
//        // Perform any processing needed and return a response
//        return ResponseEntity.ok("JAR File Uploaded Successfully");
//    }

}
