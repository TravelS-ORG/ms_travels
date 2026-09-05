package com.group.ms_travels.core.audit;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.OffsetDateTime;

@Getter
@Setter
@MappedSuperclass
@SQLRestriction("deleted_at IS NULL")
public class SoftDeletableEntity extends BaseEntity {

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "deleted_by", length = 100)
    private String deletedBy;

    public void softDelete(String userId) {
        this.deletedAt = OffsetDateTime.now();
        this.deletedBy = userId;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

}
