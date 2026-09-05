package com.group.ms_travels.module.DumpEntity;

import com.group.ms_travels.core.audit.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "dump_table")
public class DumpEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
