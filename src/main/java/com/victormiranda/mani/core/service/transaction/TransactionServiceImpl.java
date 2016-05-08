package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.dao.bankaccount.TransactionDao;
import com.victormiranda.mani.core.dto.transaction.ProcessedTransaction;
import com.victormiranda.mani.core.model.TransactionModel;
import com.victormiranda.mani.core.service.bankaccount.BankAccountService;
import com.victormiranda.mani.core.service.user.UserService;
import com.victormiranda.mani.type.TransactionFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final UserService userService;
	private final BankAccountService bankAccountService;
	private final TransactionDao transactionDao;

	@Autowired
	public TransactionServiceImpl(UserService userService, BankAccountService bankAccountService, TransactionDao transactionDao) {
		this.userService = userService;
		this.bankAccountService = bankAccountService;
		this.transactionDao = transactionDao;
	}

	@Override
	public List<ProcessedTransaction> getTransactions() {
		return transactionDao.getTransactions(userService.getCurrentUser().getId()).stream()
				.map(this::toTransaction)
				.collect(Collectors.toList());
	}

	private ProcessedTransaction toTransaction(TransactionModel tm) {
		return  new ProcessedTransaction(tm.getId(), new Category(1, "Groceries", TransactionFlow.OUT, Optional.empty()),
				tm.getBankAccount().getId(), tm.getBankAccount().getAlias(),
				tm.getUid(), tm.getDescriptionOriginal(), tm.getDate(),
				tm.getFlow(), tm.getAmount(), tm.getTransactionStatus());
	}

}
