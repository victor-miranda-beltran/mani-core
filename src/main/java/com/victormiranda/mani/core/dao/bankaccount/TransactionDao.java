package com.victormiranda.mani.core.dao.bankaccount;

import com.victormiranda.mani.core.model.TransactionModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionDao extends CrudRepository<TransactionModel, Integer> {

	@Query("select t from TransactionModel t where t.bankAccount.bankLogin.user.id = ?1")
	List<TransactionModel> getTransactions(Integer userId);
}
