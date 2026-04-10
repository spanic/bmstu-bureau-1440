package com.bmstu_bureau_1440.accounting.services;

import com.bmstu_bureau_1440.accounting.Storage;
import com.bmstu_bureau_1440.accounting.models.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryOperationsService {

    private final Storage storage;

    public boolean deleteCategory(Category category) {
        return storage.getCategories().remove(category);
    }

}
