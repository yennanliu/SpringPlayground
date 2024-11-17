package com.yen.SpringMapStruct.entity;

public enum Gender {

    Male("male"),
    Female("female");

    private String type;

    Gender(String type){
        this.type = type;
    }

    public static Gender getByType(String type) {
        //判空
        if (type == null) {
            return null;
        }
        //循环处理
        Gender[] values = Gender.values();
        for (Gender value : values) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

    public String getType(){
        return type;
    }

}
