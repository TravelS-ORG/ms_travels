package com.group.ms_travels.core.aop;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Order(1)
@Component
public class MethodExecutionTimeLoggingAdvice {

    private static final String EXEC_TIME_KEY = "exec-time-operation";

    @Around("@annotation(executionTimeLogging)")
    public Object logExecutions(ProceedingJoinPoint joinPoint, ExecutionTimeLogging executionTimeLogging) throws Throwable {

        // Retrieve the API including location and details
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        String operation = executionTimeLogging.operationName();

        String previousOperation = MDC.get(EXEC_TIME_KEY);
        MDC.put(EXEC_TIME_KEY, operation);

        // Setting up the time for the whole execution
        boolean isExecutionSuccess = true;
        StopWatch watch = new StopWatch();

        // Watch start
        watch.start();

        try {
            return joinPoint.proceed();
        } catch (Exception ex) {
            isExecutionSuccess = false;
            throw ex;
        } finally {
            // Close watch
            watch.stop();
            log.atLevel(executionTimeLogging.logLevel())
                    .log("operation={} class ={} method={} result={} timeMs={}",
                            operation, className, methodName, isExecutionSuccess ? "SUCCESS" : "FAILED", watch.getTime(TimeUnit.MILLISECONDS));

            if (previousOperation != null) {
                MDC.put(EXEC_TIME_KEY, previousOperation);
            } else {
                MDC.remove(EXEC_TIME_KEY);
            }
        }


    }
}
