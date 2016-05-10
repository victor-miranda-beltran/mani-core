package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.dao.bankaccount.BankAccountDao;
import com.victormiranda.mani.core.dao.bankaccount.TransactionDao;
import com.victormiranda.mani.core.inputtransformer.impl.ptsb.PTSBInputTransformer;
import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.model.BankTransaction;
import com.victormiranda.mani.core.service.user.UserService;
import com.victormiranda.mani.type.TransactionFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final UserService userService;
	private final TransactionDao transactionDao;
	private final BankAccountDao bankAccountDao;
	private final PTSBInputTransformer transactionTransformer;

	@Autowired
	public TransactionServiceImpl(UserService userService, TransactionDao transactionDao, BankAccountDao bankAccountDao, PTSBInputTransformer transactionTransformer) {
		this.userService = userService;
		this.transactionDao = transactionDao;
		this.bankAccountDao = bankAccountDao;
		this.transactionTransformer = transactionTransformer;
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
	public BankTransaction processTransaction(final BankAccount bankAccount, final Transaction transaction) {
		final BankTransaction newTransaction = new BankTransaction();
		final Transaction t = transactionTransformer.transform(transaction);
		newTransaction.setBankAccount(bankAccount);
		newTransaction.setId(t.getId().orElse(null));
		newTransaction.setAmount(t.getAmount());
		newTransaction.setDate(t.getDate());
		newTransaction.setDescriptionOriginal(t.getDescription());
		newTransaction.setDescriptionProcessed(t.getDescriptionProcessed());
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
		final BankAccount bankAccount = tm.getBankAccount();
		final AccountInfo accountInfo = new AccountInfo(
				bankAccount.getName(),
				bankAccount.getAccountNumber(),
				bankAccount.getUuid(),
				bankAccount.getAvailableBalance(),
				bankAccount.getCurrentBalance(),
				bankAccount.getLastSynced(),
				new HashSet<>());

		return new Transaction.Builder()
				.withId(Optional.of(tm.getId()))
				.withAccount(Optional.of(accountInfo))
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

	@Override
	public List<Transaction> reprocess() {
		List<Transaction> transactions = getTransactions().stream()
			//	.map(t -> transactionTransformer.transform(t))
				.collect(Collectors.toList());
		List<Transaction> reprocessed = new ArrayList<>();

		for(Transaction transaction : transactions) {
			BankAccount bankAccount = bankAccountDao.fetchAccount(
					userService.getCurrentUserId().get(),
					transaction.getAccount().get().getAccountNumber()).get();
			BankTransaction bankTransaction = processTransaction(bankAccount, transaction);

			reprocessed.add(toTransaction(bankTransaction));
		}
		return reprocessed;
	}

}
