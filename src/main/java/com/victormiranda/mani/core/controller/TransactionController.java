package com.victormiranda.mani.core.controller;

import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.service.transaction.TransactionFilter;
import com.victormiranda.mani.core.service.transaction.TransactionService;
import com.victormiranda.mani.core.service.transaction.TransactionTransformer;
import com.victormiranda.mani.type.TransactionFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

	@RequestMapping(value = "/transactions/{transactionId}/note", method = RequestMethod.PUT)
	public  void updateTransactionCategory(@PathVariable Integer transactionId, @RequestBody String note) {
		transactionService.updateNote(transactionId, note);
	}

	@RequestMapping(value = "/transactions/{transactionId}/category", method = RequestMethod.DELETE)
	public  void deleteTransactionCategory(@PathVariable Integer transactionId) {
		transactionService.deleteTransactionCategory(transactionId);
	}

	@RequestMapping(value = "/transactions/this-month", method = RequestMethod.GET)
	public Map getAggregation() {
		final LocalDate initial = LocalDate.now().withDayOfMonth(1);

		TransactionFilter transactionFilter = new TransactionFilter.Builder()
				.withStart(initial)
				.withFlow(TransactionFlow.OUT)
				.build();

		return transactionService.getTransactionAggregation(transactionFilter);
	}



}
