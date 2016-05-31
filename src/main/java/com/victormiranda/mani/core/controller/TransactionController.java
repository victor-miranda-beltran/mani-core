package com.victormiranda.mani.core.controller;

import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.service.transaction.TransactionService;
import com.victormiranda.mani.core.service.transaction.TransactionTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

	@RequestMapping(value = "/transactions/{transactionId}/category", method = RequestMethod.PUT)
	public  List<Transaction> updateTransactionCategory(@PathVariable Integer transactionId, @RequestBody Category category) {
		return transactionService.updateTransactionCategory(transactionId, category);
	}

	@RequestMapping(value = "/transactions/{transactionId}/category", method = RequestMethod.DELETE)
	public  void deleteTransactionCategory(@PathVariable Integer transactionId) {
		transactionService.deleteTransactionCategory(transactionId);
	}



}
