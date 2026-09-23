how to run keycloak admin console
./gradlew composeUp
open: http://localhost:8081
login: admin/admin_dev_only

Running project as master-replica/single mode:

Enforce deterministic order:

composeDownSingle
  ↓
composeDownReplica
  ↓
composeUp


- For single mode:
```bash
./gradlew startContainers -PenvMode=single
```

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

```bash
./gradlew stopContainers -PenvMode=single
```

```bash
./gradlew resetContainers -PenvMode=single
```


- For multiple mode:
```bash
./gradlew composeUp -PenvMode=multiple
```

```bash
./gradlew bootRun --args='--spring.profiles.active=local,local.replica'
```

```bash
./gradlew stopContainers -PenvMode=multiple
```

```bash
./gradlew resetContainers -PenvMode=multiple
```