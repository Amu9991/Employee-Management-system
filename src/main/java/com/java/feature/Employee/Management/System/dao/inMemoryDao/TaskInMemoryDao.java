package com.java.feature.Employee.Management.System.dao.inMemoryDao;

import com.java.feature.Employee.Management.System.dao.TaskDao;
import com.java.feature.Employee.Management.System.entities.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class TaskInMemoryDao implements TaskDao {
    private final Queue<Task> taskQueue = new ArrayDeque<>();
    private final Map<String,Task> taskMap=new HashMap<>();
    private final Map<String, List<Task>> userTasks = new HashMap<>();
    private final TreeMap<Integer, HashSet<Task>> priorityGroupedTasks = new TreeMap<>();


    @Override
    public void save(Task task) {
        log.info("Adding new task: {}", task.getName());
        if (taskMap.putIfAbsent(task.getName(), task) == null) {
            log.debug("Task {} added to taskMap", task.getName());
        if (task.getOwner() != null) {
            userTasks.putIfAbsent(task.getOwner(), new ArrayList<>());
            userTasks.get(task.getOwner()).add(task);
            log.debug("Task {} associated with owner: {}", task.getName(), task.getOwner());
        }
            if (task.getPriority() != null) {
                priorityGroupedTasks.putIfAbsent(task.getPriority(), new HashSet<>());
                priorityGroupedTasks.get(task.getPriority()).add(task);
                log.debug("Task {} added to priority group: {}", task.getName(), task.getPriority());
            }
        } else {
            log.error("Task already exists: {}", task.getName());
            throw new IllegalArgumentException("A Task already exists with the passed task name");
        }
    }

}

