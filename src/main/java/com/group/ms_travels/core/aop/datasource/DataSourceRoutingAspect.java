package com.group.ms_travels.core.aop.datasource;

import com.group.ms_travels.core.config.DynamicRoutingDataSource;
import com.group.ms_travels.core.config.datasource.MasterDatasourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceRoutingAspect {

    private final Logger logger = LoggerFactory.getLogger(DataSourceRoutingAspect.class);

    @Around("@annotation(executionOnDataSource)")
    public Object execute(ProceedingJoinPoint joinPoint, ExecuteOnDataSource dataSource) throws Throwable {
        DynamicRoutingDataSource.setCurrentLookupKey(dataSource);
        logger.info("Processing execution method: {}", joinPoint.getSignature().getName());
        try {
            return joinPoint.proceed();
        } catch (Throwable t) {
            logger.error("Error occurred while processing execution method: {}", joinPoint.getSignature().getName(), t);
            throw t;
        } finally {
            DynamicRoutingDataSource.clearCurrentLookupKey();
        }
    }

}
