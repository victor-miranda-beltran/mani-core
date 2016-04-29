package com.victormiranda.mani.core.controller;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.SynchronizationResult;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.service.bankaccount.BankAccountService;
import com.victormiranda.mani.core.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class TransactionController {

	private final BankAccountService bankAccountService;

	@Autowired
	public TransactionController(BankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}

	@RequestMapping("/transactions")
	public Set<AccountInfo> sync() {
		return bankAccountService.getAccountsInfo();
	}
}
