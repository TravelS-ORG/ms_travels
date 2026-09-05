# Core Annotation / AOP / Shared Utility Reference

This page is a quick lookup for the reusable cross-cutting features under `core/` so developers can understand how to apply them in domain/service code without tracing the AOP implementation manually.

## 1) Method execution time logging

Location:
- `src/main/java/com/group/ms_travels/core/aop/ExecutionTimeLogging.java`
- `src/main/java/com/group/ms_travels/core/aop/MethodExecutionTimeLoggingAdvice.java`

Purpose:
- Measures method execution time
- Logs `class`, `method`, `result`, and elapsed time
- Stores the current operation in MDC under `exec-time-operation`

Usage:
```java
@ExecutionTimeLogging(operationName = "Get trip detail", logLevel = Level.INFO)
public TripResponse getTrip(Long id) {
    return tripService.getById(id);
}
```

Notes:
- `logLevel` defaults to `DEBUG`
- `operationName` is optional but recommended for readable logs
- The aspect wraps methods using `@Around("@annotation(executionTimeLogging)")`
- If the method throws, the log still records `result=FAILED`

---

## 2) Functional access control (permission-based AOP)

Location:
- `src/main/java/com/group/ms_travels/core/aop/uac/UserPermission.java`
- `src/main/java/com/group/ms_travels/core/aop/uac/FunctionalAccessControl.java`
- `src/main/java/com/group/ms_travels/core/aop/uac/UserAccessControlAspect.java`
- `src/main/java/com/group/ms_travels/core/aop/uac/UserAccessHolder.java`

Purpose:
- Enforces fine-grained method-level access control before execution
- Checks permissions using an AOP `@Before` advice

Permission model:
```java
public enum UserPermission {
    READ,
    WRITE
}
```

Usage:
```java
@FunctionalAccessControl(
    requireAny = {UserPermission.READ},
    requireAll = {UserPermission.WRITE},
    errorMessage = "Trip editing requires WRITE permission"
)
public Trip updateTrip(TripRequest request) {
    // business logic
    return tripRepository.save(...);
}
```

Behavior:
- `requireAll`: all listed permissions must be allowed
- `requireAny`: at least one listed permission must be allowed
- If validation fails, throws `SecurityException`

Important note:
- `UserAccessHolder#getCurrentPermission()` is currently a local dev placeholder and returns `READ` as allowed by default.
- In production, this must be replaced by real user/session/security context data.

---

## 3) Pagination and sorting argument resolvers

Location:
- `src/main/java/com/group/ms_travels/core/paging/annos/PageableParam.java`
- `src/main/java/com/group/ms_travels/core/paging/annos/AllowedSortProperty.java`
- `src/main/java/com/group/ms_travels/core/paging/EnhancedPageableArgumentResolver.java`
- `src/main/java/com/group/ms_travels/core/paging/EnhancedSortArgumentResolver.java`
- `src/main/java/com/group/ms_travels/core/config/WebMvcConfig.java`

Purpose:
- Standardizes Spring MVC pagination/sorting across controllers
- Enforces allowed sort fields
- Uses `page_size` instead of the default `size` query param
- Keeps page numbers 1-based in request parameters

Annotations:
```java
public @interface PageableParam {
    int maxPageSize() default 100;
}
```

```java
public @interface AllowedSortProperty {
    String[] props() default {};
}
```

Controller example:
```java
@GetMapping("/trips")
public ResponseEntity<?> getTrips(
    @PageableParam(maxPageSize = 50)
    @AllowedSortProperty(props = {"id", "createdAt", "updatedAt"})
    Pageable pageable
) {
    return ResponseEntity.ok(tripService.findAll(pageable));
}
```

Request example:
- `?page=1&page_size=20&sort=createdAt,desc`

Behavior:
- `page` defaults to `1` internally (converted to zero-based for Spring)
- `page_size` defaults to `20`
- invalid sizes or negative page numbers throw `IllegalArgumentException`
- sort field must exist in `AllowedSortProperty.props()`

---

## 4) Enum validation for request payloads

Location:
- `src/main/java/com/group/ms_travels/core/validation/utils/EnumTypes/EnumValidator.java`
- `src/main/java/com/group/ms_travels/core/validation/utils/EnumTypes/EnumValidatorImpl.java`

Purpose:
- Validates String field values against a Java enum
- Works with Jakarta Bean Validation

Usage:
```java
public enum TripStatus {
    PENDING,
    ACTIVE,
    COMPLETED
}
```

```java
public class TripRequest {

    @EnumValidator(enumClass = TripStatus.class, message = "Invalid trip status")
    private String status;
}
```

Behavior:
- field value cannot be null
- comparison is case-insensitive (`toUpperCase()`)
- validation passes only when the value matches an enum constant name

---

## 5) JPA auditing and soft delete support

Location:
- `src/main/java/com/group/ms_travels/core/audit/JpaAuditConfig.java`
- `src/main/java/com/group/ms_travels/core/audit/BaseEntity.java`
- `src/main/java/com/group/ms_travels/core/audit/SoftDeletableEntity.java`

Purpose:
- Adds standard audit fields to entities
- Enables Spring Data JPA auditing automatically
- Supports soft delete behavior instead of hard delete

Base entity fields:
```java
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private Integer version;
}
```

Current audit config:
```java
@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("system");
    }
}
```

Use pattern:
```java
@Entity
public class Trip extends BaseEntity {
    private String name;
}
```

Soft delete entity:
```java
@MappedSuperclass
@SQLRestriction("deleted_at IS NULL")
public class SoftDeletableEntity extends BaseEntity {
    private OffsetDateTime deletedAt;
    private String deletedBy;
}
```

Usage:
```java
public class Trip extends SoftDeletableEntity {
    public void deleteTrip(String userId) {
        softDelete(userId);
    }
}
```

Notes:
- `deleted_at IS NULL` is applied by Hibernate so soft-deleted rows are filtered automatically
- `softDelete(String userId)` sets both deletion timestamp and actor
- Current auditor provider is a placeholder and should be upgraded to real authenticated user data later

---

## 6) Recommended usage pattern in domain/service layer

For domain/service code, prefer the following defaults:

1. Add `@ExecutionTimeLogging` on service methods that are slow, important, or difficult to trace
2. Add `@FunctionalAccessControl` on controller-facing or domain actions requiring permission checks
3. Use `@PageableParam` + `@AllowedSortProperty` on list endpoints to keep API paging safe and predictable
4. Use `@EnumValidator` for request enums instead of custom manual parsing logic
5. Extend `BaseEntity` for regular entities and `SoftDeletableEntity` for soft-delete enabled entity models

Example:
```java
@Service
public class TripService {

    @ExecutionTimeLogging(operationName = "Create trip", logLevel = Level.INFO)
    @FunctionalAccessControl(requireAny = {UserPermission.WRITE}, errorMessage = "Trip creation is not allowed")
    public TripResponse createTrip(TripRequest request) {
        // business logic
        return mapper.toResponse(repository.save(entity));
    }
}
```

---

## 7) Common caveats

- `UserAccessHolder` is still a mock and should not be treated as production auth logic yet.
- `JpaAuditConfig#auditorProvider()` returns the literal `system` user. Connect it to the authenticated principal when auth is ready.
- `@AllowedSortProperty` is strict; any unsupported sort field will fail early with `IllegalArgumentException`.
- `@EnumValidator` only validates `String`-based fields; it is not intended for numeric enum values.

This document should be used as the first reference for adding or reviewing cross-cutting behavior in the project.
