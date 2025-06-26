package com.java.feature.Employee.Management.System.dao.inMemoryDao;

import com.java.feature.Employee.Management.System.dao.AuditLogDao;
import com.java.feature.Employee.Management.System.dao.EmployeeDao;
import com.java.feature.Employee.Management.System.entities.AuditLog;
import com.java.feature.Employee.Management.System.entities.Employee;
import com.java.feature.Employee.Management.System.enums.Role;
import com.java.feature.Employee.Management.System.exception.UserNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@Slf4j
public class EmployeeInMemoryDao implements EmployeeDao {

    private final AuditLogDao auditLogDao;

    @Value("${app.admin.user.credentials.userName}")
    private String adminUserName;
    @Value("${app.admin.user.credentials.firstName}")
    private String adminfirstName;
    @Value("${app.admin.user.credentials.lastname}")
    private String adminLastName;
    @Value("${app.admin.user.credentials.department}")
    private String adminDepartment;
    @Value("${app.admin.user.credentials.middleName}")
    private String adminMiddleName;
    private int idCounter = 1;

    private final List<Employee> employees = new ArrayList<>();

    public EmployeeInMemoryDao(AuditLogDao auditLogDao) {
        this.auditLogDao = auditLogDao;
    }


    @PostConstruct
    private void init() {
        Employee employee = new Employee(idCounter++, adminfirstName, adminMiddleName, adminLastName, Role.ADMIN, adminUserName, adminDepartment, true);
        employees.add(employee);
    }

    @Override
    public void save(Employee employee) {
        if (employee.getId() == 0) {
            employee.setId(idCounter++);
        }

        employees.add(employee);
        log.info("Current employees in the system:");
        for (Employee e : employees) {
            log.info("ID={}, Name={} {}, Username={}, Department={}, Role={}",
                    e.getId(), e.getFirstName(), e.getLastName(), e.getUserName(), e.getDepartment(), e.getRole());
        }

    }

    @Override
    public List<Employee> findAll() {
        return employees.stream()
                .map(e -> {
                    Optional<AuditLog> logs = auditLogDao.getLatestLogByUser(e.getUserName());
                    logs.ifPresentOrElse(
                            log -> e.setAuditLogs(List.of(log)),
                            () -> e.setAuditLogs(Collections.emptyList())
                    );
                    return e;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Employee> findByDepartment(String department) {
        return employees.stream().filter(em -> em.getDepartment().equalsIgnoreCase(department)).collect(Collectors.toList());
    }

    @Override
    public Boolean isUserNameExist(String userName) {
        return employees.stream().anyMatch(em -> em.getUserName().equalsIgnoreCase(userName));
    }

    @Override
    public Employee findByUserName(String userName) {
        return employees.stream().filter(em -> em.getUserName().equalsIgnoreCase(userName)).findFirst()
                .orElseThrow(() -> new UserNotFoundException("Active employee not found with username: " + userName));

    }

    @Override
    public Optional<List<Employee>> findListByUserName(List<String> userNames) {
        return Optional.of(userNames.stream()
                .map(username -> employees.stream()
                        .filter(emp -> emp.getUserName().equals(username))
                        .filter(Employee::isActive) // check if active
                        .findFirst()
                        .orElseThrow(() -> new UserNotFoundException("Active employee not found with username: " + username))
                )
                .collect(Collectors.toList()));

    }


}
