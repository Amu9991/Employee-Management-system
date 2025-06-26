package com.java.feature.Employee.Management.System.exception;

public class UserNameAlreadyExistException extends RuntimeException{
   public UserNameAlreadyExistException(String meassage){
        super(meassage);
    }

    public UserNameAlreadyExistException(){
        super("Username already taken!");
    }
}
