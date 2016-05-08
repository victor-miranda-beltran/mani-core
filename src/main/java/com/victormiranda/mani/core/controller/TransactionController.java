package com.victormiranda.mani.core.controller;

import com.victormiranda.mani.core.dto.transaction.ProcessedTransaction;
import com.victormiranda.mani.core.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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
	public List<ProcessedTransaction> getTransactions() {
		return transactionService.getTransactions();
	}
}
