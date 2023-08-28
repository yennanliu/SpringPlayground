package com.yen.SpringAssignmentSystem.dto;

// https://github.com/tp02ga/AssignmentSubmissionApp/blob/master/back-end/src/main/java/com/coderscampus/AssignmentSubmissionApp/dto/AuthCredentialsRequest.java

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AuthCredentialsRequest {

    private String username;
    private String password;
}
