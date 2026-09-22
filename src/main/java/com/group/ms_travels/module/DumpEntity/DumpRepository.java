package com.group.ms_travels.module.DumpEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DumpRepository extends JpaRepository<DumpEntity, Long> {

}
