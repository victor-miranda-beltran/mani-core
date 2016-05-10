package com.victormiranda.mani.core.inputtransformer.impl;


import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.inputtransformer.InputTransformer;
import com.victormiranda.mani.core.service.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InputCategoryTransformer implements InputTransformer {

    private final CategoryService categoryService;

    @Autowired
    public InputCategoryTransformer(final CategoryService categoryService) {
        this.categoryService =  categoryService;
    }

    public Transaction transform(final Transaction transaction) {

        return transaction;
    }
}
