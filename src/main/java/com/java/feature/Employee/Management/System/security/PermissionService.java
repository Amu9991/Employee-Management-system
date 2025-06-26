package com.java.feature.Employee.Management.System.security;

import com.java.feature.Employee.Management.System.enums.Action;
import com.java.feature.Employee.Management.System.enums.Role;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Set;

@Service
@Slf4j
public class PermissionService {
    private final EnumMap<Role, Set<Action>> permissions = new EnumMap<>(Role.class);

    @PostConstruct
    public void init() {
        log.debug("Initializing permissions map");
        permissions.put(Role.ADMIN, EnumSet.of(Action.ADD, Action.UPDATE, Action.DELETE, Action.VIEW));
        permissions.put(Role.USER, EnumSet.of(Action.VIEW));
        log.info("Permissions initialized for {} roles", permissions.size());
    }

    public boolean hasPermission(Role role, Action action) {
        boolean hasPermission = permissions.getOrDefault(role, Set.of()).contains(action);
        log.debug("Permission check for role {} and action {}: {}", role, action, hasPermission);
        return hasPermission;
    }

    public Set<Action> getDefaultPermissions(Role role){

        return switch (role) {
            case ADMIN -> EnumSet.of(Action.ADD,Action.UPDATE,Action.DELETE,Action.VIEW);
            case USER -> EnumSet.of(Action.VIEW);
            default -> EnumSet.noneOf(Action.class);
        };
    }
}
