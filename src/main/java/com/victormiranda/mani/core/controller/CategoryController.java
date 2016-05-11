package com.victormiranda.mani.core.controller;

import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.service.category.CategoryService;
import com.victormiranda.mani.type.TransactionFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping("/categories")
    public List<Category> findCategories(@RequestParam final String filter,@RequestParam(required = false) final TransactionFlow flow) {
        return categoryService.findCategories(filter, Optional.ofNullable(flow));
    }
}
