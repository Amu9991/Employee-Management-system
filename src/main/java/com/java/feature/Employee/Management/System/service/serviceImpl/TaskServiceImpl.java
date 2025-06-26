package com.java.feature.Employee.Management.System.service.serviceImpl;

import com.java.feature.Employee.Management.System.dao.EmployeeDao;
import com.java.feature.Employee.Management.System.dao.TaskDao;
import com.java.feature.Employee.Management.System.dto.requestDTO.TaskRequestDTO;
import com.java.feature.Employee.Management.System.dto.responseDTO.ApiResponseDTO;
import com.java.feature.Employee.Management.System.entities.Task;
import com.java.feature.Employee.Management.System.enums.Action;
import com.java.feature.Employee.Management.System.exception.PermisssionDeniadException;
import com.java.feature.Employee.Management.System.exception.UserNotFoundException;
import com.java.feature.Employee.Management.System.service.EmployeeService;
import com.java.feature.Employee.Management.System.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskDao taskDao;
    private final EmployeeService employeeService;
    private final EmployeeDao employeeDao;

    public TaskServiceImpl(TaskDao taskDao, EmployeeServiceImpl employeeService, EmployeeDao employeeDao) {
        this.taskDao = taskDao;
        this.employeeService = employeeService;
        this.employeeDao = employeeDao;
    }

    @Override
    public ApiResponseDTO<?> createTask(String callerUserName, TaskRequestDTO taskRequestDTO) {
        log.info("Request received to create task by user: {}", callerUserName);
        try {
            log.debug("Validating user: {} for task creation permission", callerUserName);
            employeeService.checkPermissions(callerUserName, Action.ADD);
            validateEmployeeExists(taskRequestDTO.owner());
            log.debug("Creating new task with name: {}, priority: {}, category: {}",
                    taskRequestDTO.name(), taskRequestDTO.priority(), taskRequestDTO.category());
            Task task = new Task(taskRequestDTO.name(),
                    taskRequestDTO.description(),
                    taskRequestDTO.priority(),
                    taskRequestDTO.category(),
                    taskRequestDTO.deadline(),
                    callerUserName);

            log.debug("Adding task to data store");
            taskDao.save(task);

            log.info("Task created successfully for user: {}", taskRequestDTO.owner());
            return new ApiResponseDTO<>(HttpStatus.OK, "User task added successfully", false);
        } catch (PermisssionDeniadException e) {
            log.error("Permission error while creating task for user: {}. Reason: {}", callerUserName, e.getMessage());
            return new ApiResponseDTO<>(HttpStatus.FORBIDDEN, e.getMessage(), true);
        } catch (UserNotFoundException e) {
            log.error("No employees found for user : {}. Requested by user: '{}'",taskRequestDTO.owner(),callerUserName);
            return new ApiResponseDTO<>(HttpStatus.NOT_FOUND, e.getMessage(), true);
        } catch (Exception e) {
            log.error("Unexpected error while creating task. Error: {}", e.getMessage());
            return new ApiResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), true);
        }

    }

    private void validateEmployeeExists(String userName) {
        log.debug("Validating employee existence with username: '{}'", userName);
        if (!employeeDao.isUserNameExist(userName)) {
            log.warn("Username not exists: {}", userName);
            throw new UserNotFoundException("No user exists with username: " + userName);
        }
        log.debug("Validation passed for user: {}", userName);
    }
}
