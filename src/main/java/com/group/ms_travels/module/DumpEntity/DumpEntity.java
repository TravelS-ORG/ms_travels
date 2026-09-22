package com.group.ms_travels.module.DumpEntity;

import com.group.ms_travels.core.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dump_table")
public class DumpEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "number")
    private int number;

    @Column(name = "isTrue")
    private boolean isTrue;
}
