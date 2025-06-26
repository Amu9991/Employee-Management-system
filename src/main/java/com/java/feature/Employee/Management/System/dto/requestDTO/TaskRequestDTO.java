package com.java.feature.Employee.Management.System.dto.requestDTO;

import com.java.feature.Employee.Management.System.enums.Category;
import com.java.feature.Employee.Management.System.enums.TaskStatus;

import java.time.LocalDate;

public record TaskRequestDTO(String name,
                             String description,
                             Integer priority,
                             TaskStatus status,
                             Category category,
                             LocalDate deadline,
                             String owner) {
    public TaskRequestDTO{
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be empty");
        }
    }
}
