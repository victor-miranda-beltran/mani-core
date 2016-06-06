package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.dao.bankaccount.BankAccountDao;
import com.victormiranda.mani.core.dao.bankaccount.TransactionDao;
import com.victormiranda.mani.core.dao.bankaccount.TransactionSpecification;
import com.victormiranda.mani.core.inputtransformer.impl.ptsb.PTSBInputTransformer;
import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.model.BankTransaction;
import com.victormiranda.mani.core.model.TransactionCategory;
import com.victormiranda.mani.core.service.category.CategoryService;
import com.victormiranda.mani.core.service.synchronization.SyncBatch;
import com.victormiranda.mani.core.service.user.UserService;
import com.victormiranda.mani.type.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final UserService userService;
	private final CategoryService categoryService;
	private final TransactionDao transactionDao;
	private final BankAccountDao bankAccountDao;
	private final PTSBInputTransformer transactionTransformer;

	@Autowired
	public TransactionServiceImpl(UserService userService, CategoryService categoryService, TransactionDao transactionDao, BankAccountDao bankAccountDao, PTSBInputTransformer transactionTransformer) {
		this.userService = userService;
		this.categoryService = categoryService;
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

	@Override
	public List<Transaction> getPendingTransactionsFromBankAccount(final Integer bankAccountId) {
		return transactionDao.getPendingBankTransactionsByAccount(bankAccountId).stream()
				.map(this::toTransaction)
				.collect(Collectors.toList());
	}

	private Optional<BankTransaction> getTransaction(final Transaction transaction) {
		return transactionDao.findByUserAndUID(userService.getCurrentUserId().get(), transaction.getUid());
	}

	@Override
	public BankTransaction processTransaction(final Integer bankAccountId, final Transaction transaction) {
		final BankTransaction newTransaction = new BankTransaction();
		final Transaction t = transactionTransformer.transform(transaction);

		newTransaction.setBankAccount(bankAccountDao.findOne(bankAccountId));
		newTransaction.setId(t.getId().orElse(null));
		newTransaction.setAmount(t.getAmount());
		newTransaction.setBalance(t.getBalance());
		newTransaction.setDateAuthorization(t.getDateAuthorization());
		newTransaction.setDateSettled(t.getDateSettled());
		newTransaction.setDateProcessed(LocalDateTime.now());
		newTransaction.setDescriptionOriginal(t.getDescription());
		newTransaction.setDescriptionProcessed(t.getDescriptionProcessed());
		newTransaction.setUid(t.getUid());
		newTransaction.setDateSettled(LocalDate.now());
		newTransaction.setTransactionStatus(t.getStatus());
		newTransaction.setFlow(t.getFlow());

		return transactionDao.save(newTransaction);
	}

	@Override
	public Set<BankTransaction> processBatch(SyncBatch syncBatch) {
		final Set<BankTransaction> processedTransactions = new HashSet<>();

		for (Transaction transaction : syncBatch.getNewTransactions()) {
			processedTransactions.add(processTransaction(syncBatch.getAccountId(), transaction));
		}

		for (Transaction transaction : syncBatch.getUpdatedTransactions()) {
			processedTransactions.add(processTransaction(syncBatch.getAccountId(), transaction));
		}

		for (Transaction transaction : syncBatch.getDeletedTransactions()) {
			BankTransaction transactionToDelete = transactionDao.findOne(transaction.getId().get());
			transactionDao.delete(transactionToDelete);
		}

		return processedTransactions;
	}

	@Override
	public  List<Transaction> updateTransactionCategory(final Integer transactionId, final Category category) {
		final List<Transaction> modifiedTransactions = new ArrayList<>();
		final BankTransaction paramTransaction = transactionDao.findOne(transactionId);
		final Set<BankTransaction> similarTransactions =
				transactionDao.findSimilarTransactionsWithoutCategory(
						paramTransaction.getDescriptionProcessed(), userService.getCurrentUserId().get());

		for (BankTransaction bankTransaction : similarTransactions) {
			final TransactionCategory transactionCategory = new TransactionCategory();

			transactionCategory.setId(category.getId());
			transactionCategory.setName(category.getName());
			transactionCategory.setFlow(category.getFlow());

			bankTransaction.setCategory(transactionCategory);

			transactionDao.save(bankTransaction);

			modifiedTransactions.add(toTransaction(bankTransaction));
		}

		return modifiedTransactions;
	}

	public void deleteTransactionCategory(final Integer transactionId) {
		final BankTransaction paramTransaction = transactionDao.findOne(transactionId);

		paramTransaction.setCategory(null);
		transactionDao.save(paramTransaction);
	}

	private Transaction toTransaction(BankTransaction tm) {
		final BankAccount bankAccount = tm.getBankAccount();

		final AccountInfo accountInfo = new AccountInfo.Builder()
				.withId(bankAccount.getId())
				.withName(bankAccount.getName())
				.withAlias(bankAccount.getAlias())
				.withAccountNumber(bankAccount.getAccountNumber())
				.build();

		final Optional<Category> category =
				Optional.ofNullable(categoryService.fromTransactionCategory(tm.getCategory()));

		return new Transaction.Builder()
				.withId(Optional.of(tm.getId()))
				.withAccount(accountInfo)
				.withCategory(category)
				.withUid(tm.getUid())
				.withDescription(tm.getDescriptionOriginal())
				.withDescriptionProcessed(tm.getDescriptionProcessed())
				.withDateAuthorization(tm.getDateAuthorization())
				.withDateSettled(tm.getDateSettled())
				.withAmount(tm.getAmount())
				.withFlow(tm.getFlow())
				.withStatus(tm.getTransactionStatus())
				.build();
	}

	@Override
	public List<Transaction> getSettledTransactions(AccountInfo accountInfo) {
		final Integer userId = userService.getCurrentUserId().get();

		//very poor implementation, WIP
		final List<Transaction> transactions = accountInfo.getTransactions().stream()
				.filter(t -> t.getStatus() == TransactionStatus.NORMAL)
				.map(t -> transactionDao.findByUserAndUID(userId, t.getUid()))
				.filter(t -> t.isPresent())
				.map(t -> toTransaction(t.get()))
				.collect(Collectors.toList());

		return transactions;
	}

	@Override
	public Map<Category, BigDecimal> getTransactionAggregation(final TransactionFilter transactionFilter) {
		final List<BankTransaction> bankTransactions =
				transactionDao.findAll(new TransactionSpecification(transactionFilter));

		Map<Category, BigDecimal> res = bankTransactions.stream()
				.map(this::toTransaction)
				.collect(Collectors.groupingBy(t -> t.getCategory().orElse(new Category(null, "Uncategorized", t.getFlow(), Optional.empty())),
						Collectors.mapping(Transaction::getAmount,
								Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));

		return res;
	}


}
