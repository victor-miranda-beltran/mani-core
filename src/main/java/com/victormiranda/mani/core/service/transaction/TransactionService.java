package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.model.BankTransaction;

import java.util.List;

public interface TransactionService {

	List<Transaction> getTransactions();

	BankTransaction processTransaction(BankAccount bankAccount, Transaction t);

	List <BankTransaction> processTransactions(BankAccount bankAccount, AccountInfo accountInfo);

	Transaction toTransaction(BankTransaction tm);

	List<Transaction> reprocess();
}
