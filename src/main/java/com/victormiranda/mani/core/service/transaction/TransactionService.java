package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.model.BankTransaction;
import com.victormiranda.mani.core.service.synchronization.SyncBatch;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TransactionService {

	List<Transaction> getTransactions();

	List <Transaction> getPendingTransactionsFromBankAccount(Integer bankAccountId);

	BankTransaction processTransaction(Integer bankAccountId, Transaction t);

	Set<BankTransaction> processBatch(final SyncBatch syncBatch);

	List<Transaction> updateTransactionCategory(final Integer transactionId, Category category);

	void deleteTransactionCategory(final Integer transactionId);

	List<Transaction> getSettledTransactions(AccountInfo accountInfo);

	Map<Category, BigDecimal> getTransactionAggregation(TransactionFilter transactionFilter);
}
