package com.bmstu_bureau_1440.library.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Column;
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
    @Column("client_id")
    private AggregateReference<Client, Long> client;
    @Column("book_id")
    private AggregateReference<Book, Long> book;
    private OperationType type;
    @CreatedDate
    private LocalDateTime performedAt;
}
