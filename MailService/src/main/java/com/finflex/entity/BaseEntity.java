package com.finflex.entity;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    Boolean state = true;

    @CreationTimestamp
    LocalDateTime createdDate;

    @UpdateTimestamp
    LocalDateTime updatedDate;
}
