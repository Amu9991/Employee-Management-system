package com.java.feature.Employee.Management.System.service;

import com.java.feature.Employee.Management.System.dto.requestDTO.TaskRequestDTO;
import com.java.feature.Employee.Management.System.dto.responseDTO.ApiResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface TaskService {
    public ApiResponseDTO<?> createTask(String callerUserName, TaskRequestDTO taskRequestDTO);
}
