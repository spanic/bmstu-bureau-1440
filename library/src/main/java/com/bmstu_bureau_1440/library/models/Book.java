package com.bmstu_bureau_1440.library.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("books")
public class Book {
    @Id
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private boolean available;
}
