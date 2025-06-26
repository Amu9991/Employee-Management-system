package com.java.feature.Employee.Management.System.entities;

import com.java.feature.Employee.Management.System.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class Employee {
@Id
private int id;
    private String firstName;
    private String middleName;
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String userName;
    private String department;
    private boolean isActive;
    private List<AuditLog> auditLogs;

    public Employee(){}

    public Employee(int id,String firstName, String middleName, String lastName,
                    Role role, String userName, String department, boolean isActive,List<AuditLog> auditLogs) {
        this.id=id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.role = role;
        this.userName = userName;
        this.department = department;
        this.isActive = isActive;
        this.auditLogs=auditLogs;
    }
    public Employee(int id,String firstName, String middleName, String lastName,
                    Role role, String userName, String department, boolean isActive) {
        this.id=id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.role = role;
        this.userName = userName;
        this.department = department;
        this.isActive = isActive;
    }

}
