package com.java.feature.Employee.Management.System.dto.other;

import com.java.feature.Employee.Management.System.enums.Action;
import com.java.feature.Employee.Management.System.enums.Role;
import java.time.LocalDateTime;

public record AuditLogDTO(
        int id,
        LocalDateTime timestamp,
        Role role,
        Action action,
        String details,
        String userName) {
}
