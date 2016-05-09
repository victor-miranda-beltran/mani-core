package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.dto.transaction.ProcessedTransaction;
import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.model.BankTransaction;

import java.util.Collection;
import java.util.List;

public interface TransactionService {

	List<ProcessedTransaction> getTransactions();

	BankTransaction saveTransaction(BankAccount bankAccount, Transaction t);

	List <BankTransaction> processTransactions(BankAccount bankAccount, AccountInfo accountInfo);
}
