package com.java.feature.Employee.Management.System.dto.responseDTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
public class ApiResponseDTO<T> {
    private T data;
    private List<T> dataList;
    private HttpStatus status;
    private boolean error;
    String message;

    public ApiResponseDTO(HttpStatus status,String message,boolean error){
        this.status=status;
        this.message=message;
        this.error=error;
    }

    public ApiResponseDTO(T data,HttpStatus status,String message,boolean error){
        this.data=data;
        this.status=status;
        this.message=message;
        this.error=error;
    }
}
