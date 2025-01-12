package com.finflex.entitiy;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseEntity {

    @Builder.Default
    Boolean state = true;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @CreatedDate
    private LocalDateTime updatedDate;

}
