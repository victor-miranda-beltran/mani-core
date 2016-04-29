package com.victormiranda.mani.core.service.bankaccount;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Credentials;
import com.victormiranda.mani.bean.SynchronizationResult;
import com.victormiranda.mani.core.model.BankLogin;

import java.util.Set;

public interface BankAccountService {

	Credentials getLoginCredentials(final BankLogin bankLogin);

	void updateBankAccounts(final BankLogin bankLogin, final SynchronizationResult synchronizationResult);

	Set<AccountInfo> getAccountsInfo();

	Set<AccountInfo> getAccountsInfo(BankLogin bankLogin);
}
