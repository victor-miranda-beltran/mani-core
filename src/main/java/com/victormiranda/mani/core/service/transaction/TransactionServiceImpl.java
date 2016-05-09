package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.dao.bankaccount.TransactionDao;
import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.model.BankTransaction;
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
	public List<Transaction> getTransactions() {
		return transactionDao.getTransactions(userService.getCurrentUserId().get()).stream()
				.map(this::toTransaction)
				.collect(Collectors.toList());
	}

	private Optional<BankTransaction> getTransaction(final BankAccount bankAccount, final Transaction transaction) {
		return transactionDao.findByUserAndUID(userService.getCurrentUserId().get(), transaction.getUid());
	}

	@Override
	public BankTransaction processTransaction(final BankAccount bankAccount, final Transaction t) {
		final BankTransaction newTransaction = new BankTransaction();

		newTransaction.setBankAccount(bankAccount);
		newTransaction.setAmount(t.getAmount());
		newTransaction.setDate(t.getDate());
		newTransaction.setDescriptionOriginal(t.getDescription());
		newTransaction.setDescriptionProcessed("ozu");
		newTransaction.setUid(t.getUid());

		newTransaction.setTransactionStatus(t.getStatus());
		newTransaction.setFlow(t.getFlow());

		return transactionDao.save(newTransaction);
	}

	@Override
	public List<BankTransaction> processTransactions(BankAccount bankAccount, AccountInfo accountInfo) {
		return accountInfo.getTransactions().stream()
				.filter(t -> !getTransaction(bankAccount, t).isPresent())
				.map(t -> processTransaction(bankAccount, t))
				.collect(Collectors.toList());
	}

	@Override
	public Transaction toTransaction(BankTransaction tm) {
		return new Transaction.Builder()
				.withId(Optional.of(tm.getId()))
				.withCategory(Optional.of(new Category(1, "Groceries", TransactionFlow.OUT, Optional.empty())))
				.withUid(tm.getUid())
				.withDescription(tm.getDescriptionOriginal())
				.withDescriptionProcessed(tm.getDescriptionProcessed())
				.withDate(tm.getDate())
				.withAmount(tm.getAmount())
				.withFlow(tm.getFlow())
				.withStatus(tm.getTransactionStatus())
				.build();
	}

}
