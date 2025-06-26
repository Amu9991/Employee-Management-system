package com.java.feature.Employee.Management.System.dao;

import com.java.feature.Employee.Management.System.entities.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeDao {
    void save(Employee employee);
    List<Employee> findAll();
    List<Employee> findByDepartment(String department);
    Boolean isUserNameExist(String userName);
    Optional<List<Employee>> findListByUserName(List<String> userNames);
    Employee findByUserName(String userName);
}
