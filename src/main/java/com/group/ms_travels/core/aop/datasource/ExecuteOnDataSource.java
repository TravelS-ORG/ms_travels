package com.group.ms_travels.core.aop.datasource;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ExecuteOnDataSource {

    String dataSource();
}
