package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.dao.bankaccount.TransactionDao;
import com.victormiranda.mani.core.dto.transaction.ProcessedTransaction;
import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.model.BankTransaction;
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
	private final TransactionDao transactionDao;

	@Autowired
	public TransactionServiceImpl(UserService userService, TransactionDao transactionDao) {
		this.userService = userService;
		this.transactionDao = transactionDao;
	}

	@Override
	public List<ProcessedTransaction> getTransactions() {
		return transactionDao.getTransactions(userService.getCurrentUserId().get()).stream()
				.map(this::toTransaction)
				.collect(Collectors.toList());
	}

	private Optional<BankTransaction> getTransaction(final BankAccount bankAccount, final Transaction transaction) {
		return transactionDao.findByUserAndUID(userService.getCurrentUserId().get(), transaction.getTransactionUID());
	}

	@Override
	public BankTransaction saveTransaction(final BankAccount bankAccount, final Transaction t) {
		final BankTransaction newTransaction = new BankTransaction();

		newTransaction.setBankAccount(bankAccount);
		newTransaction.setAmount(t.getAmount());
		newTransaction.setDate(t.getDate());
		newTransaction.setDescriptionOriginal(t.getDescription());
		newTransaction.setUid(t.getTransactionUID());

		newTransaction.setTransactionStatus(t.getStatus());
		newTransaction.setFlow(t.getFlow());

		return transactionDao.save(newTransaction);
	}

	@Override
	public List<BankTransaction> processTransactions(BankAccount bankAccount, AccountInfo accountInfo) {
		return accountInfo.getTransactions().stream()
				.filter(t -> !getTransaction(bankAccount, t).isPresent())
				.map(t -> saveTransaction(bankAccount, t))
				.collect(Collectors.toList());
	}

	private ProcessedTransaction toTransaction(BankTransaction tm) {
		return  new ProcessedTransaction(tm.getId(), new Category(1, "Groceries", TransactionFlow.OUT, Optional.empty()),
				tm.getBankAccount().getId(), tm.getBankAccount().getAlias(),
				tm.getUid(), tm.getDescriptionOriginal(), tm.getDate(),
				tm.getFlow(), tm.getAmount(), tm.getTransactionStatus());
	}

}
