package com.yen.SpringBootFileUpload1.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

/**
 *  File upload controller
 */
@RestController
public class UploadController {


    // TODO : check this
    private static final String PATH = new File("upload").getAbsolutePath();

    @PostMapping("/upload")
    public String uploadFileByParam(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) throws IOException {
        File dir = new File(PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        // getOriginalFilename获取其原始文件名
        file.transferTo(new File(PATH + File.separator + file.getOriginalFilename()));
        return name + " UPLOAD SUCCESS ！";
    }

}
