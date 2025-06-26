package com.java.feature.Employee.Management.System.dto.responseDTO;
import com.java.feature.Employee.Management.System.dto.other.AuditLogDTO;
import com.java.feature.Employee.Management.System.enums.Role;
import java.util.List;

public record EmployeeResonseDTO  (int id,
                                   String firstName,
                                   String middleName,
                                   String lastName,
                                   Role role,
                                   String userName,
                                   String department,
                                   boolean isActive,
                                   List<AuditLogDTO> auditLogDTO
) {

        public EmployeeResonseDTO {
            if (firstName == null || firstName.trim().isEmpty()
                    ||middleName == null || middleName.trim().isEmpty()
                    ||lastName == null || lastName.trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be empty");
            }
            if (userName == null || userName.trim().isEmpty()) {
                throw new IllegalArgumentException("Department cannot be empty");
            }

            if (department == null || department.trim().isEmpty()) {
                throw new IllegalArgumentException("Department cannot be empty");
            }
        }
}
