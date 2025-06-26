package com.java.feature.Employee.Management.System.dao;

import com.java.feature.Employee.Management.System.entities.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDao {
void save(Task task);
}
