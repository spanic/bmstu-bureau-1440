package com.bmstu_bureau_1440.library.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("operations")
public class Operation {
    @Id
    private Long id;
    private Long bookId;
    private OperationType type;
    private LocalDateTime performedAt;
    private String borrowerName;
}
