package com.java.feature.Employee.Management.System.service;

import com.java.feature.Employee.Management.System.dto.requestDTO.EmployeeRequestDTO;
import com.java.feature.Employee.Management.System.dto.requestDTO.UserNameUpdateDTO;
import com.java.feature.Employee.Management.System.dto.responseDTO.ApiResponseDTO;
import com.java.feature.Employee.Management.System.dto.responseDTO.EmployeeResonseDTO;
import com.java.feature.Employee.Management.System.entities.Employee;
import com.java.feature.Employee.Management.System.enums.Action;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public interface EmployeeService {
public ApiResponseDTO<Employee> addEmployee(EmployeeRequestDTO employeeRequestDTO, String callerUserName);
public ApiResponseDTO<List<EmployeeResonseDTO>> listEmployee(int page, int size, String callerUserName);
public ApiResponseDTO<?> deleteEmployee(String callerUserName,String userName);
public ApiResponseDTO<?> bactchUpdateFullName(String callerUserName, List<UserNameUpdateDTO> userNameUpdateDTO);
public void checkPermissions(String callerUserName, Action action);
}
