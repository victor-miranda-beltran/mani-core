package com.victormiranda.mani.core.service.bankaccount;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Credentials;
import com.victormiranda.mani.bean.SynchronizationResult;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.ptsb.PTSBCredentials;
import com.victormiranda.mani.core.dao.bankaccount.BankAccountDao;
import com.victormiranda.mani.core.dao.bankaccount.TransactionDao;
import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.model.BankLogin;
import com.victormiranda.mani.core.model.TransactionModel;
import com.victormiranda.mani.core.model.User;
import com.victormiranda.mani.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BankAccountServiceImpl implements BankAccountService {

	private final BankAccountDao bankAccountDao;
	private final TransactionDao transactionDao;
	private final UserService userService;

	@Autowired
	public BankAccountServiceImpl(
			BankAccountDao bankAccountDao,
			TransactionDao transactionDao,
			UserService userService) {
		this.bankAccountDao = bankAccountDao;
		this.transactionDao = transactionDao;
		this.userService = userService;
	}

	public Credentials getLoginCredentials(final BankLogin bankLogin) {
		return new PTSBCredentials(bankLogin.getField1(), bankLogin.getField2(), bankLogin.getField3());
	}

	@Override
	public void updateBankAccounts(final BankLogin bankLogin, final SynchronizationResult synchronizationResult) {
		final Set<BankAccount> bankAccounts = synchronizationResult.getAccounts()
				.stream()
				.map(ai -> toBankAccount(bankLogin, ai))
				.collect(Collectors.toSet());
	}

	@Override
	public Set<AccountInfo> getAccountsInfo() {
		final User user = userService.getCurrentUser();
		return bankAccountDao.fetchAccounts(user.getId()).stream()
				.map(this::toAccountInfo)
				.collect(Collectors.toSet());
	}

	@Override
	public Set<AccountInfo> getAccountsInfo(final BankLogin bankLogin) {
		return bankLogin.getBankAccounts().stream()
				.map(this::toAccountInfo)
				.collect(Collectors.toSet());
	}

	private AccountInfo toAccountInfo(final BankAccount bankAccount) {
		final String id = bankAccount.getName();
		final String uuid = bankAccount.getUuid();
		final BigDecimal availableBalance = bankAccount.getAvailableBalance();
		final BigDecimal currentBalance = bankAccount.getCurrentBalance();
		final LocalDate lastSynced = bankAccount.getLastSynced();

		final Set<Transaction> transactions = bankAccount.getTransactions().stream()
				.map(tm -> toDTOTransaction(tm)).collect(Collectors.toSet());

		return new AccountInfo(id, uuid, availableBalance, currentBalance, lastSynced,transactions);
	}

	private BankAccount toBankAccount(final BankLogin bankLogin, final AccountInfo accountInfo) {
		BankAccount bankAccount = getOrCreate(bankLogin, accountInfo);

		//process transactions
		final List<TransactionModel> transactionModels = accountInfo.getTransactions().stream()
				.filter(t -> !exists(bankAccount, t))
				.map(t -> toDBTransaction(bankAccount, t))
				.collect(Collectors.toList());

		bankAccount.getTransactions().addAll(transactionModels);

		return bankAccount;
	}

	private boolean exists(final BankAccount bankAccount, final Transaction transaction) {
		return bankAccount.getTransactions().stream()
				.anyMatch(t -> t.getUid() != null && t.getUid().equals(transaction.getTransactionUID()));
	}

	private TransactionModel toDBTransaction(final BankAccount bankAccount, final Transaction t) {
		final TransactionModel newTransaction = new TransactionModel();

		newTransaction.setBankAccount(bankAccount);
		newTransaction.setAmount(t.getAmount());
		newTransaction.setDate(t.getDate());
		newTransaction.setDescriptionOriginal(t.getDescription());
		newTransaction.setUid(t.getTransactionUID());
		newTransaction.setTransactionStatus(t.getStatus());
		newTransaction.setFlow(t.getFlow());

		return transactionDao.save(newTransaction);
	}

	private Transaction toDTOTransaction(TransactionModel tm) {
		return new Transaction(tm.getUid(),tm.getDescriptionOriginal(), tm.getDate(), tm.getFlow(), tm.getAmount(), tm.getTransactionStatus());
	}

	private BankAccount getOrCreate(BankLogin bankLogin, AccountInfo accountInfo) {
		final User user = userService.getCurrentUser();
		user.setId(1);
		final BankAccount bankAccount = bankAccountDao.fetchAccount(user.getId(), accountInfo.getId())
				.orElseGet(() -> createBankAccount(bankLogin, accountInfo));

		return bankAccount;
	}

	public BankAccount createBankAccount(final BankLogin bankLogin, final AccountInfo accountInfo) {
		final BankAccount bankAccount = new BankAccount();

		bankAccount.setName(accountInfo.getId());
		bankAccount.setCurrentBalance(accountInfo.getCurrentBalance());
		bankAccount.setAvailableBalance(accountInfo.getAvailableBalance());
		bankAccount.setLastSynced(LocalDate.now());
		bankAccount.setUuid(accountInfo.getUid());
		bankAccount.setBankLogin(bankLogin);

		return bankAccountDao.save(bankAccount);
	}
}
