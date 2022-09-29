package com.yen.springUserSystem.bean;

// book p.2-21

import lombok.*;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private String code;
    private String message;
    private T data;

    /** NOTICE this !!! */
    public static <T> ApiResponse<T> success(String code, String message, T data){
        ApiResponse<T> response = new ApiResponse<T>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }

}
