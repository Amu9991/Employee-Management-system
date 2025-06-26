package com.java.feature.Employee.Management.System.entities;

import com.java.feature.Employee.Management.System.enums.Category;
import com.java.feature.Employee.Management.System.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private String name;
    private String description;
    private Integer priority;
    private TaskStatus status = TaskStatus.Created;
    private Category category;
    private LocalDate deadline;
    private String owner;

    public Task(String name, String description, Integer priority, Category category, LocalDate deadline, String owner) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.deadline = deadline;
        this.owner = owner;
    }
}
