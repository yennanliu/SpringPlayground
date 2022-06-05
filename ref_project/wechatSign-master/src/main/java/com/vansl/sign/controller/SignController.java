package com.vansl.sign.controller;

import com.vansl.sign.entity.Sign;
import com.vansl.sign.service.SignService;
import com.vansl.sign.vo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SignController {

    @Autowired
    SignService signService;

    /*
    * 发起签到
    * */
    @PostMapping(value = "/course/{courseId}/sign")
    public HttpResult add(@PathVariable Long courseId) {
        HttpResult result = new HttpResult();
        try{
            if (signService.findSignsNotEndByCourse(courseId).size()!=0) {
                throw new Exception("本课程有签到尚未结束，请先结束签到！");
            }
            Sign sign = new Sign();
            sign.setEnd(false);
            sign.setCourseId(courseId);
            signService.save(sign);
            result.setStatus("ok");
        }catch (Exception e){
            if(!e.getMessage().isEmpty()){
                result.setMessage(e.getMessage());
            }
            result.setStatus("wrong");
        }finally {
            return result;
        }

    }

    /*
    * 结束签到
    * */
    @PutMapping(value = "/sign/{id}/end")
    public HttpResult end(@PathVariable Long id) {
        HttpResult result = new HttpResult();
        try{
            if (signService.signIsEnd(id)){
                throw new Exception("签到已经结束");
            }
            Sign sign = signService.findSignById(id);
            sign.setEnd(true);
            signService.save(sign);
            result.setStatus("ok");
        }catch (Exception e){
            if(!e.getMessage().isEmpty()){
                result.setMessage(e.getMessage());
            }
            result.setStatus("wrong");
        }finally {
            return result;
        }

    }


    /*
    * 查询课程所有发起的签到
    * */
    @GetMapping(value = "/course/{courseId}/signs")
    public HttpResult querySignsByCourse(@PathVariable Long courseId){
         HttpResult<List<Sign>> result = new HttpResult<>();
         List<Sign> signs = signService.findSignsByCourse(courseId);
         result.setData(signs);
         result.setStatus("ok");
         return result;
    }

}
