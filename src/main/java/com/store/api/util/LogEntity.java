package com.store.api.util;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class LogEntity {

    @CreatedBy
    @Column(insertable = true,updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(insertable = true,updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(insertable = false,updatable = true)
    private String modifiedBy;

    @LastModifiedDate
    @Column(insertable = false,updatable = true)
    private LocalDateTime modifiedAt;
}