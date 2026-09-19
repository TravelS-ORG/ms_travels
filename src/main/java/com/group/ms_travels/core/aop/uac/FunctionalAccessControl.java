package com.group.ms_travels.core.aop.uac;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FunctionalAccessControl {
    UserPermission[] requireAny() default {};
    UserPermission[] requireAll() default {};
    String errorMessage() default "Access Forbidden";

}

