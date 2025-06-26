package com.java.feature.Employee.Management.System.dto.requestDTO;

import com.java.feature.Employee.Management.System.enums.Role;

public record EmployeeRequestDTO(
                                 String firstName,
                                 String middleName,
                                 String lastName,
                                 Role role,
                                 String userName,
                                 String department,
                                 boolean isActive) {
    public EmployeeRequestDTO {
        if (firstName == null || firstName.trim().isEmpty()
                ||middleName == null || middleName.trim().isEmpty()
                ||lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }

        if (department == null || department.trim().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }
    }
}
