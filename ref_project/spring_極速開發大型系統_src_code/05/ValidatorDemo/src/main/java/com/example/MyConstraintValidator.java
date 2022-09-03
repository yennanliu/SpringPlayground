﻿package com.example;

import com.example.demo.MyConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class MyConstraintValidator implements ConstraintValidator<MyConstraint, String> {
    //String為驗證的型態
    @Override
    public void initialize(MyConstraint myConstraint) {
        //啟動時執行
    }

    /**
     * @Description: 自訂驗證邏輯
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext validatorContext) {
        if (!(s.equals("北京") || s.equals("上海"))) {
            return false;
        }

        return true;
    }
}
