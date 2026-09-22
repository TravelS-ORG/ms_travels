package com.group.ms_travels.core.config;

import com.group.ms_travels.core.audit.log.LazyLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Collections;

@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<Object> currentLookupKey = new ThreadLocal<>();

    public static void setCurrentLookupKey(Object lookupKey) {
        currentLookupKey.set(lookupKey);
    }

    public static Object getCurrentLookupKey() {
        return currentLookupKey.get();
    }

    public static void clearCurrentLookupKey() {
        Object currentLook = getCurrentLookupKey();
        LazyLogUtil.info(log, "Removing current lookup key: " + currentLook, Collections::emptyList);
        currentLookupKey.remove();
    }
    @Override
    protected @Nullable Object determineCurrentLookupKey() {
        return currentLookupKey.get();
    }
}
