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
    private Genre genre;
    private boolean available;

    @Override
    public String toString() {
        return String.format("%d. \"%s\", Автор: %s, Жанр: %s", id, title, author, genre);
    }

}
