package com.victormiranda.mani.core.service.category;


import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.model.TransactionCategory;
import com.victormiranda.mani.type.TransactionFlow;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    List<Category> findCategories(String filter, Optional<TransactionFlow> transactionFlow);

    Category fromTransactionCategory(final TransactionCategory transactionCategory);
}
