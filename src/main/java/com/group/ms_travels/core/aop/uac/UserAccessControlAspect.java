package com.group.ms_travels.core.aop.uac;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.*;

@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class UserAccessControlAspect {

    private final UserAccessHolder userAccessHolder;

    @Before("annotation(functionalAccessControl)")
    public void validate(JoinPoint joinPoint, FunctionalAccessControl functionalAccessControl) {
        UserPermission[] requireAll = functionalAccessControl.requireAll();
        UserPermission[] requireAny = functionalAccessControl.requireAny();

        if (ArrayUtils.isEmpty(requireAll) && ArrayUtils.isEmpty(requireAny)) {
            return;
        }

        Map<UserPermission, Allowance> userPermissionAllowanceMap = MapUtils.emptyIfNull(userAccessHolder.getCurrentPermission());

        List<UserPermission> missingRoles = new ArrayList<>();

        Arrays.stream(requireAll)
                .filter(userPermission -> Allowance.NOT_ALLOWED.equals(
                        userPermissionAllowanceMap.getOrDefault(userPermission, Allowance.NOT_ALLOWED)))
                .forEach(missingRoles::add);

        if (ArrayUtils.isNotEmpty(requireAny)) {
            boolean hasAny = Arrays.stream(requireAny)
                    .anyMatch(userPermission -> Allowance.ALLOW.equals(userPermissionAllowanceMap.getOrDefault(userPermission, Allowance.NOT_ALLOWED)));

            if (!hasAny) {
                missingRoles.add(requireAny[0]);
            }
        }

        if (CollectionUtils.isNotEmpty(missingRoles)) {
            throw new SecurityException(functionalAccessControl.errorMessage() + " - missing=" + missingRoles);
        }
    }
}
