package com.java.feature.Employee.Management.System.entities;

import com.java.feature.Employee.Management.System.enums.Action;
import com.java.feature.Employee.Management.System.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Action action;
    private String details;
    private String userName;


    public AuditLog(int id,LocalDateTime timestamp, Role role, Action action, String details, String userName) {
        this.id=id;
        this.timestamp = timestamp;
        this.role = role;
        this.action = action;
        this.details = details;
        this.userName = userName;
    }
}
