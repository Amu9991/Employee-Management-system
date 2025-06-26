package com.java.feature.Employee.Management.System.controller.controllerImpl;

import com.java.feature.Employee.Management.System.controller.EmployeeController;
import com.java.feature.Employee.Management.System.dto.requestDTO.EmployeeRequestDTO;
import com.java.feature.Employee.Management.System.dto.requestDTO.UserNameUpdateDTO;
import com.java.feature.Employee.Management.System.dto.responseDTO.ApiResponseDTO;
import com.java.feature.Employee.Management.System.dto.responseDTO.EmployeeResonseDTO;
import com.java.feature.Employee.Management.System.entities.Employee;
import com.java.feature.Employee.Management.System.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeControllerImpl implements EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeControllerImpl(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Override
    public ResponseEntity<ApiResponseDTO<Employee>> addEmployee(EmployeeRequestDTO employeeRequestDTO, String callerUserName) {
        return ResponseEntity.ok(employeeService.addEmployee(employeeRequestDTO, callerUserName));

    }

    @Override
    public ResponseEntity<ApiResponseDTO<List<EmployeeResonseDTO>>> getEmployeeList(int page, int size, String callerUserName) {
        return ResponseEntity.ok(employeeService.listEmployee(page, size, callerUserName));

    }

    @Override
    public ResponseEntity<ApiResponseDTO<?>> deleteEmployee(String callerUserName, String userName) {
        return ResponseEntity.ok(employeeService.deleteEmployee(callerUserName,userName));

    }

    @Override
    public ResponseEntity<ApiResponseDTO<?>> batchUpadateFullName(String callerUserName, List<UserNameUpdateDTO> userNameUpdateDTOs) {
        return ResponseEntity.ok(employeeService.bactchUpdateFullName(callerUserName,userNameUpdateDTOs));
    }
}
