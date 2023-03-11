package com.yen.gulimall.common.constant;

// https://youtu.be/Ga6NMrVkRDY?t=443

public class ProductConstant {

    public enum AttrEnum{
        ATTR_TYPE_BASE(1, "base type"),
        ATTR_TYPE_SALE(0, "sales type");

        AttrEnum(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public String getMsg(){
            return msg;
        }

    }
}
