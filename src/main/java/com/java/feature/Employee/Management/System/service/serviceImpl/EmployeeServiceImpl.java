package com.java.feature.Employee.Management.System.service.serviceImpl;

import com.java.feature.Employee.Management.System.dao.AuditLogDao;
import com.java.feature.Employee.Management.System.dao.EmployeeDao;
import com.java.feature.Employee.Management.System.dto.other.AuditLogDTO;
import com.java.feature.Employee.Management.System.dto.requestDTO.EmployeeRequestDTO;
import com.java.feature.Employee.Management.System.dto.requestDTO.UserNameUpdateDTO;
import com.java.feature.Employee.Management.System.dto.responseDTO.ApiResponseDTO;
import com.java.feature.Employee.Management.System.dto.responseDTO.EmployeeResonseDTO;
import com.java.feature.Employee.Management.System.entities.AuditLog;
import com.java.feature.Employee.Management.System.entities.Employee;
import com.java.feature.Employee.Management.System.enums.Action;
import com.java.feature.Employee.Management.System.enums.Role;
import com.java.feature.Employee.Management.System.exception.PermisssionDeniadException;
import com.java.feature.Employee.Management.System.exception.UserNameAlreadyExistException;
import com.java.feature.Employee.Management.System.exception.UserNotFoundException;
import com.java.feature.Employee.Management.System.security.PermissionService;
import com.java.feature.Employee.Management.System.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final PermissionService permissionService;
    private final EmployeeDao employeeDao;
    private final AuditLogDao auditLogDao;

    public EmployeeServiceImpl(PermissionService permissionService, EmployeeDao employeeDao, AuditLogDao auditLogDao) {
        this.permissionService = permissionService;
        this.employeeDao = employeeDao;
        this.auditLogDao = auditLogDao;
    }

    @Override
    public ApiResponseDTO<Employee> addEmployee(EmployeeRequestDTO employeeDTO, String callerUserName) {
        log.info("Request received to add employee with username: '{}' by user: {}.", employeeDTO.userName(), callerUserName);

        try {
            checkPermissions(callerUserName,Action.ADD);
            validateEmployeeExists(employeeDTO.userName());

            log.debug("Logging action {} for role {}", Action.ADD,callerUserName);
            AuditLog auditLog=new AuditLog(0,LocalDateTime.now(),Role.ADMIN,Action.ADD,"Added new employee",callerUserName);
            auditLogDao.save(auditLog,employeeDTO.userName());

            log.debug("Creating new employee with username: {}", employeeDTO.userName());
            Employee employee = new Employee(0,employeeDTO.firstName(), employeeDTO.middleName(), employeeDTO.lastName(), employeeDTO.role(), employeeDTO.userName(), employeeDTO.department(),true);

            log.debug("Adding employee to data store");
            employeeDao.save(employee);

            log.info("Employee added successfully");
            return new ApiResponseDTO<>(HttpStatus.OK, "Employee added Successfully", false);

        } catch (PermisssionDeniadException e) {
            log.error("Permission error while adding Employee: {}. Error: {}", employeeDTO.userName(), e.getMessage());
            return new ApiResponseDTO<>(HttpStatus.FORBIDDEN, e.getMessage(), true);
        } catch (UserNameAlreadyExistException e) {
            log.error("Employee already exists with user name : {}. Error: {}", employeeDTO.userName(), e.getMessage());
            return new ApiResponseDTO<>(HttpStatus.CONFLICT, e.getMessage(), true);
        } catch (Exception e) {
            log.error("Unexpected error while adding employee with user: {}. Error: {}", employeeDTO.userName(), e.getMessage());
            return new ApiResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), true);

        }
    }

    @Override
    public ApiResponseDTO<List<EmployeeResonseDTO>> listEmployee(int page, int size,String callerUserName) {
        log.info("Request received to fetch employee list by user: {} with page: {}, size: {}", callerUserName, page, size);
        try {
            checkPermissions(callerUserName,Action.VIEW);

            log.debug("Fetching employees from database for pagination: page={}, size={}", page, size);
           List<EmployeeResonseDTO> employeeList = Optional.of(employeeDao.findAll().stream()
                           .skip((long) page * size).limit(size).filter(Employee::isActive).map(this::toEmploeeyDTO)
                           .collect(Collectors.toUnmodifiableList())).filter(l->!l.isEmpty())
                   .orElseThrow(()->new UserNotFoundException("No employees found for the requested page."));

            log.info("Successfully fetched {} employees", employeeList.size());
            return new ApiResponseDTO<>(employeeList, HttpStatus.FOUND, "Employee list fetch successfully",false);


        } catch (PermisssionDeniadException e) {
            log.error("Permission error while fetching employee list for user: {}. Reason: {}", callerUserName, e.getMessage());
            return new ApiResponseDTO<>(HttpStatus.FORBIDDEN, e.getMessage(), true);
        }catch (UserNotFoundException e){
            log.error("No employees found for page: {}, size: {}. Requested by user: '{}'", page, size,callerUserName);
            return new ApiResponseDTO<>(HttpStatus.NOT_FOUND, e.getMessage(), true);
        }
        catch (Exception e) {
            log.error("Unexpected error while fetching employee list. Error: {}", e.getMessage());
            return new ApiResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), true);
        }
    }

    @Override
    public ApiResponseDTO<?> deleteEmployee(String callerUserName, String userName) {
        log.info("Request received to delete employee : {} by userName: {}", userName, callerUserName);
        try {
            checkPermissions(callerUserName,Action.DELETE);

            log.debug("Attempting to delete employee with username: '{}'", userName);
            Optional<Employee> employee = employeeDao.findAll().stream().filter(e -> e.getUserName().equalsIgnoreCase(userName)).findFirst();

            employee.ifPresentOrElse(e -> e.setActive(false), () -> {
                log.warn("Attempted to delete employee, but no record found for username: '{}'", userName);
                throw new IllegalArgumentException("Employee with username '" + userName + "' not found");
            });

            log.debug("Logging action {} for role {}", Action.ADD,callerUserName);
            AuditLog auditLog=new AuditLog(0,LocalDateTime.now(),Role.ADMIN,Action.DELETE,"Deleted employee",callerUserName);
            auditLogDao.save(auditLog,userName);

            log.info("Employee with username '{}' deactivated successfully", userName);
            return new ApiResponseDTO<>(HttpStatus.OK, "Employee with username : " + userName + " deactivated successfully", false);

        } catch (PermisssionDeniadException e) {
            log.error("Permission error while deleting employee with user name: {}. Error: {}", userName, e.getMessage());
            return new ApiResponseDTO<>(HttpStatus.FORBIDDEN, e.getMessage(), true);
        } catch (IllegalArgumentException e) {
            log.error("Delete operation failed: Employee with username '{}' not found", userName);
            return new ApiResponseDTO<>(HttpStatus.NOT_FOUND, e.getMessage(), true);
        } catch (Exception e) {
            log.error("Unexpected error while deleting employee with user: {}. Error: {}", userName, e.getMessage());
            return new ApiResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), true);
        }
    }


    public ApiResponseDTO<?> bactchUpdateFullName(String callerUserName, List<UserNameUpdateDTO> userNameUpdateDTOs) {
        log.info("Request received to update employee full name by userName: {}", callerUserName);
        try {
            checkPermissions(callerUserName, Action.UPDATE);

            log.info("Starting batch update of employee names. Initiated by: {}", callerUserName);

            userNameUpdateDTOs.forEach(dto -> {
                Employee employee = employeeDao.findByUserName(dto.userName());

                StringBuilder changeDetails = new StringBuilder("Updated employee with userName: " + employee.getUserName() + ". Changes: ");
                if (!Objects.equals(employee.getFirstName(), dto.firstName())) {
                    changeDetails.append("firstName: '").append(employee.getFirstName()).append("' → '").append(dto.firstName()).append("'; ");
                    employee.setFirstName(dto.firstName());
                }
                if (!Objects.equals(employee.getMiddleName(), dto.middleName())) {
                    changeDetails.append("middleName: '").append(employee.getMiddleName()).append("' → '").append(dto.middleName()).append("'; ");
                    employee.setMiddleName(dto.middleName());
                }
                if (!Objects.equals(employee.getLastName(), dto.lastName())) {
                    changeDetails.append("lastName: '").append(employee.getLastName()).append("' → '").append(dto.lastName()).append("'; ");
                    employee.setLastName(dto.lastName());
                }

                log.debug("Saving updated employee: {}", employee.getUserName());
                employeeDao.save(employee);

                AuditLog auditLog = new AuditLog(
                        0,
                        LocalDateTime.now(),
                        Role.ADMIN,
                        Action.UPDATE,
                        changeDetails.toString(),
                        callerUserName
                );
                auditLogDao.save(auditLog, employee.getUserName());

                log.info("Audit log saved for employee update: {}", employee.getUserName());
            });

            log.info("Batch update completed successfully by {}", callerUserName);
            return new ApiResponseDTO<>(HttpStatus.OK, "Employee full names updated successfully", false);

        } catch (PermisssionDeniadException e) {
            log.error("Permission denied for user {} during batch update. Error: {}", callerUserName, e.getMessage());
            return new ApiResponseDTO<>(HttpStatus.FORBIDDEN, e.getMessage(), true);

        } catch (UserNotFoundException e) {
            log.error("User not found during batch update. Error: {}", e.getMessage());
            return new ApiResponseDTO<>(HttpStatus.NOT_FOUND, e.getMessage(), true);

        } catch (IllegalArgumentException e) {
            log.error("Invalid input during batch update. Error: {}", e.getMessage());
            return new ApiResponseDTO<>(HttpStatus.BAD_REQUEST, e.getMessage(), true);

        } catch (Exception e) {
            log.error("Unexpected error during batch update by {}. Error: {}", callerUserName, e.getMessage());
            return new ApiResponseDTO<>(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), true);
        }
    }


    private EmployeeResonseDTO toEmploeeyDTO(Employee e) {
        log.debug("Mapping Employee entity to DTO: userName={}",e.getUserName());
        return new EmployeeResonseDTO(e.getId(),e.getFirstName(), e.getMiddleName(), e.getLastName(), e.getRole(), e.getUserName(), e.getDepartment(), e.isActive(),toAuditLogDTO(e.getAuditLogs()));
    }

    private List<AuditLogDTO> toAuditLogDTO(List<AuditLog> auditLogs) {
        log.debug("Mapping list of AuditLog entities to DTOs. Total logs: {}", auditLogs.size());
        return auditLogs.stream()
                .map(a -> new AuditLogDTO(
                        a.getId(),
                        a.getTimestamp(),
                        a.getRole(),
                        a.getAction(),
                        a.getDetails(),
                        a.getUserName()))
                .collect(Collectors.toList());
    }


    private void validateEmployeeExists(String userName) {
        log.debug("Validating employee existence with username: '{}'", userName);
        if (employeeDao.isUserNameExist(userName)) {
            log.warn("Username already exists: {}", userName);
            throw new UserNameAlreadyExistException();
        }
        log.debug("Validation passed for user: {}", userName);
    }

    public void checkPermissions(String callerUserName,Action action) {
        log.debug("Checking permissions for userName: {}", callerUserName);
        Optional<Employee> employee=employeeDao.findAll().stream().filter(e->e.getUserName().equalsIgnoreCase(callerUserName)).findFirst();
        employee.ifPresentOrElse(
                emp -> {
                    if (!permissionService.hasPermission(employee.get().getRole(),action)) {
                        log.warn("Permission denied for role: {}",employee.get().getRole());
                        throw new PermisssionDeniadException("No permission to "+action+" employee");
                    }
                },
                () -> {
                    throw new UserNotFoundException("User with username '" + callerUserName + "' not found");
                }
        );

        log.debug("Permissions passed for userName: {} with role: {}", callerUserName,employee.get().getRole());
    }
}
