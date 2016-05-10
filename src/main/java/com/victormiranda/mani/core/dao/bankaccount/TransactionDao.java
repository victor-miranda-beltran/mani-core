package com.victormiranda.mani.core.dao.bankaccount;

import com.victormiranda.mani.core.model.BankTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionDao extends CrudRepository<BankTransaction, Integer> {

	@Query("select t from BankTransaction t where t.bankAccount.bankLogin.user.id = ?1")
	List<BankTransaction> getTransactions(final Integer userId);

	@Query("select t from BankTransaction t where t.uid = ?2 and t.uid != '' and t.bankAccount.bankLogin.user.id = ?1")
	Optional<BankTransaction> findByUserAndUID(final Integer userId, final String uid);
}
