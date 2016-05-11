package com.victormiranda.mani.core.service.category;


import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.dao.CategoryDao;
import com.victormiranda.mani.core.model.TransactionCategory;
import com.victormiranda.mani.type.TransactionFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryDao categoryDao;

    @Autowired
    public CategoryServiceImpl(final CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<Category> findCategories(String filter, Optional<TransactionFlow> flow) {
        final List<TransactionCategory> categories = flow.isPresent()
                ? categoryDao.filterWithFlow(filter, flow.get())
                : categoryDao.filter(filter);

        return categories.stream()
                .map(tc -> toCategoryBean(tc))
                .collect(Collectors.toList());
    }

    @Override
    public Category fromTransactionCategory(final TransactionCategory transCat) {
        if (transCat == null) {
            return null;
        }

        final Optional<Category> parent = Optional.empty();
        return new Category(transCat.getId(), transCat.getName(), transCat.getFlow(), parent);
    }


    public Category toCategoryBean(final TransactionCategory transactionCategory) {
        return new Category(transactionCategory.getId(), transactionCategory.getName(), TransactionFlow.OUT, Optional.empty());
    }
}
