package com.victormiranda.mani.core.service.bankaccount;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Credentials;
import com.victormiranda.mani.bean.SynchronizationResult;
import com.victormiranda.mani.bean.ptsb.PTSBCredentials;
import com.victormiranda.mani.core.dao.bankaccount.BankAccountDao;
import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.model.BankLogin;
import com.victormiranda.mani.core.model.BankTransaction;
import com.victormiranda.mani.core.model.User;
import com.victormiranda.mani.core.service.transaction.TransactionService;
import com.victormiranda.mani.core.service.user.UserService;
import com.victormiranda.mani.core.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class BankAccountServiceImpl implements BankAccountService {

	private final BankAccountDao bankAccountDao;
	private final UserService userService;
	private final TransactionService transactionService;

	@Autowired
	public BankAccountServiceImpl(
			BankAccountDao bankAccountDao,
			UserService userService,
			TransactionService transactionService) {
		this.bankAccountDao = bankAccountDao;
		this.userService = userService;
		this.transactionService = transactionService;
	}

	@Override
	public Credentials getLoginCredentials(final BankLogin bankLogin) {
		return new PTSBCredentials(bankLogin.getField1(), bankLogin.getField2(), bankLogin.getField3());
	}

	@Override
	public void updateBankAccounts(final BankLogin bankLogin, final SynchronizationResult synchronizationResult) {
		for (AccountInfo accountInfo : synchronizationResult.getAccounts()) {
			toBankAccount(bankLogin, accountInfo);
		}
	}

	@Override
	public Set<AccountInfo> getAccountsInfo() {
		final User user = userService.getCurrentUser().get();
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

	@Override
	public Set<AccountInfo> getAccountsInfoByUserId(final Integer userId) {
		return bankAccountDao.fetchAccounts(userId).stream()
				.map(this::toAccountInfo)
				.collect(Collectors.toSet());
	}

	@Override
	public Map<LocalDate, BigDecimal> getAccountBalanceInTime(final AccountInfo accountInfo) {
		final Map<LocalDate, BigDecimal> evolution = new LinkedHashMap<>();
		final BankAccount bankAccount = bankAccountDao.findOne(accountInfo.getId());

		List<BankTransaction> transactions = bankAccount.getTransactions();

		final Comparator<BankTransaction> comp = (o1, o2) -> o1.getDateAuthorization().compareTo(o2.getDateAuthorization());

		final LocalDate start = transactions.stream().min(comp).get().getDateAuthorization();

		final LocalDate end = transactions.stream().max(comp).get().getDateAuthorization();

		BigDecimal balance = transactions.stream().min(comp).get().getBalance();

		for (LocalDate day : DateUtils.getDateRange(start, end)) {
			Optional<BankTransaction> lastTransactionOfDay = transactions.stream().filter(
					t -> t.getDateAuthorization().equals(day)).findFirst();

			if (lastTransactionOfDay.isPresent()) {
				balance = lastTransactionOfDay.get().getBalance();
			}

			evolution.put(day, balance);

		}

		return evolution;
	}

	public BankAccount createBankAccount(final BankLogin bankLogin, final AccountInfo accountInfo) {
		final BankAccount bankAccount = new BankAccount();

		bankAccount.setName(accountInfo.getName());
		// by default, alias is the name
		bankAccount.setAlias(accountInfo.getName());
		bankAccount.setAccountNumber(accountInfo.getAccountNumber());
		bankAccount.setCurrentBalance(accountInfo.getCurrentBalance());
		bankAccount.setAvailableBalance(accountInfo.getAvailableBalance());
		bankAccount.setLastSynced(LocalDate.now());
		bankAccount.setUuid(accountInfo.getUid());
		bankAccount.setBankLogin(bankLogin);
		bankAccount.setTransactions(new ArrayList<>());

		return bankAccountDao.save(bankAccount);
	}

	private AccountInfo toAccountInfo(final BankAccount bankAccount) {

		final AccountInfo accountInfo = new AccountInfo.Builder()
				.withName(bankAccount.getName())
				.withAccountNumber(bankAccount.getAccountNumber())
				.withUid(bankAccount.getUuid())
				.withAvailableBalance(bankAccount.getAvailableBalance())
				.withCurrentBalance(bankAccount.getCurrentBalance())
				.withLastSynced(bankAccount.getLastSynced())
				.build();

		return accountInfo;
	}

	private BankAccount toBankAccount(final BankLogin bankLogin, final AccountInfo accountInfo) {
		final BankAccount bankAccount = getOrCreate(bankLogin, accountInfo);

		final List<BankTransaction> bankTransactions = transactionService.processTransactions(bankAccount.getId(), accountInfo);

		if (bankTransactions != null && bankTransactions.size() > 0) {
			bankAccount.getTransactions().addAll(bankTransactions);
		}

		return bankAccountDao.save(bankAccount);
	}

	private BankAccount getOrCreate(BankLogin bankLogin, AccountInfo accountInfo) {
		final User user = userService.getCurrentUser().get();

		final BankAccount bankAccount = bankAccountDao.fetchAccount(user.getId(), accountInfo.getAccountNumber())
				.orElseGet(() -> createBankAccount(bankLogin, accountInfo));

		return bankAccount;
	}
}
