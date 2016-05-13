package com.victormiranda.mani.core.controller;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.service.bankaccount.BankAccountService;
import com.victormiranda.mani.core.service.transaction.TransactionService;
import com.victormiranda.mani.core.service.transaction.TransactionTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class BankAccountController {

	private final BankAccountService bankAccountService;

	@Autowired
	private TransactionTransformer transactionTransformer;

	@Autowired
	public BankAccountController(BankAccountService bankAccountService) {
		this.bankAccountService = bankAccountService;
	}

	@RequestMapping("/balanceEvolution")
	public Map<LocalDate, BigDecimal> getBalanceEvolution() {
		return bankAccountService.getAccountBalanceInTime(new AccountInfo.Builder().withId(4).build());
	}





}
