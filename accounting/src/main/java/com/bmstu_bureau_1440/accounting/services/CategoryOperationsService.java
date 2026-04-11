package com.bmstu_bureau_1440.accounting.services;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.Category;
import com.bmstu_bureau_1440.accounting.models.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryOperationsService {

    private final Storage storage;

    public Category addNewCategory(String name, OperationType type) {
        Category category = new Category(type, name);
        storage.getCategories().add(category);
        return category;
    }

    public boolean deleteCategory(Category category) {
        return storage.getCategories().remove(category);
    }

}
