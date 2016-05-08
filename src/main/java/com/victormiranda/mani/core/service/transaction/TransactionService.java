package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.core.dto.transaction.ProcessedTransaction;

import java.util.List;

public interface TransactionService {

	List<ProcessedTransaction> getTransactions();
}
