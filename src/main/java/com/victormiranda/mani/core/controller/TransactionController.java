package com.victormiranda.mani.core.controller;

import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {

	private final TransactionService transactionService;

	@Autowired
	public TransactionController(TransactionService transactionService) {
		this.transactionService = transactionService;
	}

	@RequestMapping("/transactions")
	public List<Transaction> getTransactions() {
		return transactionService.getTransactions();
	}

	@RequestMapping("/serial")
	public String sendTransactions(@RequestBody List<Transaction> transactions){

		return transactions.toString();
	}
}
