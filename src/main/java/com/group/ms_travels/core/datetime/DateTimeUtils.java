package com.group.ms_travels.core.datetime;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

// Setting this class for date and time utility methods (timezone)
/*
* Following on one source of truth - DateTimeUtil.now(), following on tz, not depend from JVM
* Following on time zone property (VN timezone)
* Following on one result (ae dev ko cai nhau)
 */
public final class DateTimeUtils {

    private DateTimeUtils() {}

    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    // Returns the current date and time in the default time zone
    public static OffsetDateTime now() {
        return OffsetDateTime.now(DEFAULT_ZONE);
    }

    // Returns the start of the day for the given date in UTC
    public static OffsetDateTime startOfDay(LocalDate date) {
        return date == null ? null : date.atStartOfDay().atOffset(ZoneOffset.UTC);
    }

    // Returns the end of the day for the given date in UTC
    public static OffsetDateTime endOfDay(LocalDate date) {
        return date == null ? null : date.plusDays(1).atStartOfDay().atOffset(ZoneOffset.UTC).minusNanos(1);
    }

}
