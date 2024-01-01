package com.yen.FlinkRestService.Controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UploadJarDto {

    private String jarFile;
//    private String fileName;
//    private Date uploadTime;
}
