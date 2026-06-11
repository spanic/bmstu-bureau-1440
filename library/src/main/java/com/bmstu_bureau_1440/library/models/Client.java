package com.bmstu_bureau_1440.library.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("clients")
public class Client {

    @Id
    private Long id;
    private String name;
    private String email;
    @CreatedDate
    private LocalDateTime createdAt;

    @Override
    public String toString() {
        return String.format("%d. %s, (%s). Добавлен: %s",
                id, name, StringUtils.isBlank(email) ? "e-mail не указан" : email,
                createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
    }

}
