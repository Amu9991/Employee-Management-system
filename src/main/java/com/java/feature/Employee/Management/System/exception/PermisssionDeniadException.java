package com.java.feature.Employee.Management.System.exception;

public class PermisssionDeniadException extends RuntimeException{

    public PermisssionDeniadException(String message){
        super(message);
    }
   public PermisssionDeniadException(){
        super("The requester user has no permission to perform the required operation");
    }


}
