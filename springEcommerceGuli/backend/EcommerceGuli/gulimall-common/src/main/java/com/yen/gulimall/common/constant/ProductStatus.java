package com.yen.gulimall.common.constant;

// https://youtu.be/PZW2rOit2s8?t=1102

public class ProductStatus {

    public enum AttrEnum{
        NEW_SPU(0, "create new spu"),
        SPU_UP(1, "spu up shelf"),
        SPU_DOWN(2, "spu down shelf");

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
