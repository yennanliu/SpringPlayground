package com.yen.gulimall.common.constant;

// https://youtu.be/aIDrxpLHylw?t=663

public class WareConstant {

    public enum  PurchaseStatusEnum{

        CREATED(0, "created"),
        ASSIGNED(1, "assigned"),
        RECEIVED(2, "received"),
        FINISHED(3, "finished"),
        HASERROR(4, "error"),

        ;
        private int code;
        private String msg;

        PurchaseStatusEnum(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode(){
            return code;
        }

        private String getMsg(){
            return msg;
        }
    }

    // https://youtu.be/aIDrxpLHylw?t=907
    public enum  PurchaseDetailStatusEnum{

        CREATED(0, "created"),
        ASSIGNED(1, "assigned"),
        BUYING(2, "buying"),
        FINISHED(3, "finished"),
        HASERROR(4, "buy failed"),

        ;
        private int code;
        private String msg;

        PurchaseDetailStatusEnum(int code, String msg){
            this.code = code;
            this.msg = msg;
        }

        public int getCode(){
            return code;
        }

        private String getMsg(){
            return msg;
        }
    }

}
