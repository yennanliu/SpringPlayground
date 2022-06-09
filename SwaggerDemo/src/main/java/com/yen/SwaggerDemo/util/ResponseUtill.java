//package com.yen.SwaggerDemo.util;
//
//// https://github.com/niumoo/springboot/blob/master/springboot-web-swagger/src/main/java/net/codingme/boot/utils/ResponseUtill.java
//
//import io.swagger.models.Response;
//import org.springframework.util.StringUtils;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//public class ResponseUtill {
//
//
//    public static Response success(Object objects) {
//        ArrayList list = new ArrayList<>();
//        if (!(objects instanceof Collection)) {
//            list.add(objects);
//        } else {
//            list = (ArrayList) objects;
//        }
//        return new Response(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage(), list);
//    }
//
//    public static Response success() {
//        return success(new ArrayList<>());
//    }
//
//    public static Response success(String message) {
//        Response success = success(new ArrayList<>());
//        if (!StringUtils.isEmpty(message)) {
//            success.setMessage(message);
//        }
//        return success;
//    }
//
//    public static Response error(String code, String message) {
//        return new Response(code, message, new ArrayList<>());
//    }
//
//    public static Response error(ResponseEnum responseEnum) {
//        return error(responseEnum.getCode(), responseEnum.getMessage());
//    }
//
//    public static Response error(ResponseEnum responseEnum, String message) {
//        return error(responseEnum.getCode(), message);
//    }
//
//}
