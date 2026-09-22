package com.group.ms_travels.module.DumpEntity;

import com.group.ms_travels.core.aop.datasource.ExecuteOnDataSource;
import com.group.ms_travels.core.aop.execution.ExecutionTimeLogging;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.Dump;
import org.slf4j.event.Level;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static com.group.ms_travels.core.config.datasource.MasterDatasourceConfig.MASTER_SRC_KEY;
import static com.group.ms_travels.core.config.datasource.MasterDatasourceConfig.REPLICA_SRC_KEY;


@Service
public class DumpService {

    private final DumpRepository dumpRepository;

    public DumpService(DumpRepository dumpRepository) {
        this.dumpRepository = dumpRepository;
    }

    @ExecutionTimeLogging(logLevel = Level.INFO, operationName = "getDumpEntitiesByValue")
    @ExecuteOnDataSource(dataSource = REPLICA_SRC_KEY)
    public List<DumpEntity> getDumpEntitiesByValue() {
        return dumpRepository.findAll();
    }

    @ExecutionTimeLogging(logLevel = Level.INFO, operationName = "createDumpEntity")
    @ExecuteOnDataSource(dataSource = MASTER_SRC_KEY)
    @Transactional
    public DumpEntity creatDumpEntity(DumpEntity dumpEntity) {
        DumpEntity entity = new DumpEntity();
        entity.setId(dumpEntity.getId());
        entity.setValue(dumpEntity.getValue());
        entity.setNumber(dumpEntity.getNumber());
        entity.setTrue(true);
        return dumpRepository.save(entity);
    }
}
