package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.dto.transaction.ProcessedTransaction;

import java.util.List;
import java.util.Set;

public interface TransactionService {

	List<ProcessedTransaction> getTransactions();
}
