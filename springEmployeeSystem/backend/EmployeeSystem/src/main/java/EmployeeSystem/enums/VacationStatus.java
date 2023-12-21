package EmployeeSystem.enums;

public enum VacationStatus {

    PENDING("PENDING"),
    CREATED("CREATED"),
    REVIEWING("REVIEWING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    CANCELLED("CANCELLED");

    // attr
    private String name;

    // constructor
    VacationStatus(String name){
        this.name = name;
    }

    // getter, setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
