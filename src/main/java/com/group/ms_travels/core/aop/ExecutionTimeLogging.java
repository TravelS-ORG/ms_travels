package com.group.ms_travels.core.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.slf4j.event.Level;
import org.springframework.core.annotation.AliasFor;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ExecutionTimeLogging {
    Level logLevel() default Level.DEBUG;

    @AliasFor("logLevel")
    Level value() default Level.DEBUG;

    String operationName() default "";
}
