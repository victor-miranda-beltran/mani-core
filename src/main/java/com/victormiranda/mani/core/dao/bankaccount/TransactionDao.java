package com.victormiranda.mani.core.dao.bankaccount;

import com.victormiranda.mani.core.model.BankTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TransactionDao extends CrudRepository<BankTransaction, Integer> {

	@Query("select t from BankTransaction t where t.bankAccount.bankLogin.user.id = ?1")
	List<BankTransaction> getTransactions(final Integer userId);

	@Query("select t from BankTransaction t where t.uid = ?2 and t.uid != '' and t.bankAccount.bankLogin.user.id = ?1")
	Optional<BankTransaction> findByUserAndUID(final Integer userId, final String uid);

	@Query("select t from BankTransaction t where t.bankAccount.id = ?1 AND t.transactionStatus = 'PENDING'")
	List<BankTransaction> getPendingBankTransactionsByAccount(Integer bankAccountId);

	@Query("select t from " +
			"BankTransaction t " +
			"	WHERE  t.category IS NULL AND t.bankAccount.bankLogin.user.id = ?2 AND t.descriptionProcessed = ?1")
	Set<BankTransaction> findSimilarTransactionsWithoutCategory(String descriptionProcessed, final Integer userId);
}
