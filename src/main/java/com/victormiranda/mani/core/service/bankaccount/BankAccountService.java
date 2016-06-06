package com.victormiranda.mani.core.service.bankaccount;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Credentials;
import com.victormiranda.mani.bean.SynchronizationResult;
import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.model.BankLogin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BankAccountService {

	Credentials getLoginCredentials(final BankLogin bankLogin);

	List<BankAccount> syncBankAccounts(final BankLogin bankLogin, final SynchronizationResult synchronizationResult);

	Set<AccountInfo> getAccountsInfo();

	Set<AccountInfo> getAccountsInfo(BankLogin bankLogin);

	Set<AccountInfo> getAccountsInfoByUserId(Integer userId);

	Set<BalanceEvolution> getAccountsBalanceEvolution();

	AccountInfo getAccountById(int id);
}
