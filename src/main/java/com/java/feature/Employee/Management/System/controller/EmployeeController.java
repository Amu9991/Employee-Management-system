package com.java.feature.Employee.Management.System.controller;

import com.java.feature.Employee.Management.System.dto.requestDTO.EmployeeRequestDTO;
import com.java.feature.Employee.Management.System.dto.requestDTO.UserNameUpdateDTO;
import com.java.feature.Employee.Management.System.dto.responseDTO.ApiResponseDTO;
import com.java.feature.Employee.Management.System.dto.responseDTO.EmployeeResonseDTO;
import com.java.feature.Employee.Management.System.entities.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public interface EmployeeController {

    @PostMapping("/add/{callerUserName}")
    ResponseEntity<ApiResponseDTO<Employee>> addEmployee(@RequestBody EmployeeRequestDTO employeeRequestDTO, @PathVariable("callerUserName") String callerUserName);

    @GetMapping("/get/{callerUserName}")
    ResponseEntity<ApiResponseDTO<List<EmployeeResonseDTO>>> getEmployeeList(@RequestParam int page,
                                                                             @RequestParam int size,
                                                                             @PathVariable(value = "callerUserName",required = true) String callerUserName
                                                         );

    @DeleteMapping("/delete/{callerUserName}/{userName}")
    ResponseEntity<ApiResponseDTO<?>> deleteEmployee(@PathVariable("callerUserName") String callerUserName, @PathVariable("userName") String userName);

    @PutMapping("/full-name/update/{callerUserName}")
    ResponseEntity<ApiResponseDTO<?>> batchUpadateFullName(@PathVariable("callerUserName")String callerUserName,@RequestBody List<UserNameUpdateDTO> userNameUpdateDTOs);

}
