package com.example.demo.util.result;

/**
 * @author longzhonghua
 * @data 2019/01/14 15:30
 */


public class ResultUtil {


    //傳參的成功
    public static Result success(Integer code,String msg,Object object){
        Result result=new Result();
        result.setCode(200);
        result.setMsg("動作成功");
        result.setData(object);
        return result;

    }

    //不傳參的成功
    public static Result success(){
        return success(200,"動作成功",null);
    }
    //傳參的失敗
    public static Result error(Integer code,String msg){
        Result result=new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }


}

