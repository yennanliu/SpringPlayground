package EmployeeSystem.exception;

public class CustomException extends IllegalArgumentException{

    // constructor
    public CustomException(String msg){
        super(msg);
    }
}
