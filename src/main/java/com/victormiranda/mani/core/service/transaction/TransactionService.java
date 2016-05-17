package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.model.BankTransaction;

import java.util.List;

public interface TransactionService {

	List<Transaction> getTransactions();

	List <Transaction> getPendingTransactionsFromBankAccount(Integer bankAccountId);

	BankTransaction processTransaction(Integer bankAccountId, Transaction t);

	List<BankTransaction> processPendingTransactions(Integer bankAccountId, List<Transaction> newPendings);

	List <BankTransaction> processSettledTransactions(Integer bankAccountId, AccountInfo accountInfo);

	Transaction updateTransactionCategory(final Integer transactionId, Category category);

	List<Transaction> reprocess();
}
