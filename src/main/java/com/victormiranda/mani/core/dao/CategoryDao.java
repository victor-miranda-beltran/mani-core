package com.victormiranda.mani.core.dao;

import com.victormiranda.mani.core.model.TransactionCategory;
import com.victormiranda.mani.type.TransactionFlow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao extends CrudRepository<TransactionCategory, Integer> {

    @Query("select c from TransactionCategory c where c.flow = ?2 AND c.name LIKE %?1%")
    List<TransactionCategory> filterWithFlow(final String filter, final TransactionFlow transactionFlow);

    @Query("select c from TransactionCategory c where c.name LIKE %?1%")
    List<TransactionCategory> filter(final String filter);

}
