package com.yen.mdblog.constant;

public enum PageConst {

    PAGE_SIZE(3),
    PAGE_NUM(0);

    PageConst(int size){
        this.size = size;
    }

    private int size;

    public int getSize(){
        return this.size;
    }


}
