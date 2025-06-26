package com.java.feature.Employee.Management.System.dao;

import com.java.feature.Employee.Management.System.entities.AuditLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuditLogDao {
    AuditLog save(AuditLog logEntry,String targetUserName);
    List<AuditLog> findAll();
    List<AuditLog> getAllLatestLog();
    Optional<AuditLog> getLatestLogByUser(String userName);
}
