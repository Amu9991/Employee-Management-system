package com.java.feature.Employee.Management.System.dao.inMemoryDao;

import com.java.feature.Employee.Management.System.dao.AuditLogDao;
import com.java.feature.Employee.Management.System.entities.AuditLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class AuditLogInMemoryDao implements AuditLogDao {
    private static int idCounter = 1;
    private List<AuditLog> auditLogs = new ArrayList<>();
    private ConcurrentHashMap<String, List<AuditLog>> auditslogsByUser = new ConcurrentHashMap<>();

    @Override
    public AuditLog save(AuditLog logEntry, String targetUserName) {
        log.debug("Saving audit log entry with user name {}", logEntry.getUserName());
        logEntry.setId(idCounter++);
        auditLogs.add(logEntry);
        auditslogsByUser.computeIfAbsent(targetUserName, k -> new ArrayList<>()).add(logEntry);
        log.info("Audit log entry saved: {} - {}", logEntry.getAction(), logEntry.getDetails());
        return logEntry;
    }

    @Override
    public List<AuditLog> findAll() {
        log.debug("Retrieving all audit log entries, count: {}", auditLogs.size());
        return Collections.unmodifiableList(auditLogs);
    }

    @Override
    public List<AuditLog> getAllLatestLog() {
        return auditslogsByUser.values().stream()
                .filter(list -> !list.isEmpty()) // avoid empty lists
                .map(list -> list.get(list.size() - 1)) // get last element from each list
                .toList();
    }

    @Override
    public Optional<AuditLog> getLatestLogByUser(String userName) {
        return Optional.ofNullable(auditslogsByUser.get(userName))
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream().reduce((first, second) -> second)
                )
                .orElse(Optional.empty());
    }


}
