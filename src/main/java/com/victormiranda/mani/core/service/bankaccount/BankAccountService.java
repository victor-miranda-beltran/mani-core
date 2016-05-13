package com.victormiranda.mani.core.service.bankaccount;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Credentials;
import com.victormiranda.mani.bean.SynchronizationResult;
import com.victormiranda.mani.core.model.BankLogin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public interface BankAccountService {

	Credentials getLoginCredentials(final BankLogin bankLogin);

	void updateBankAccounts(final BankLogin bankLogin, final SynchronizationResult synchronizationResult);

	Set<AccountInfo> getAccountsInfo();

	Set<AccountInfo> getAccountsInfo(BankLogin bankLogin);

	Set<AccountInfo> getAccountsInfoByUserId(Integer userId);

	Map<LocalDate, BigDecimal> getAccountBalanceInTime(AccountInfo accountInfo);
}
