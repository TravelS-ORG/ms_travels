package com.group.ms_travels.core.aop.uac;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class UserAccessHolder {
    public Map<UserPermission, Allowance> getCurrentPermission() {
        Map<UserPermission, Allowance> permissions = new EnumMap<>(UserPermission.class);
        permissions.put(UserPermission.READ, Allowance.ALLOW); // Default as READ in local dev
        return permissions;
    }
}
