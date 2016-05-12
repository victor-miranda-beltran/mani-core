package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.model.BankTransaction;

import java.util.List;

public interface TransactionService {

	List<Transaction> getTransactions();

	BankTransaction processTransaction(Transaction t);

	List <BankTransaction> processTransactions(AccountInfo accountInfo);

	Transaction updateTransactionCategory(final Integer transactionId, Category category);

	List<Transaction> reprocess();
}
