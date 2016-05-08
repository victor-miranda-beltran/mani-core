package com.victormiranda.mani.core.dto.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.type.TransactionFlow;
import com.victormiranda.mani.type.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProcessedTransaction extends Transaction {

	private Integer id;
	private Integer accountId;
	private String accountName;
	private Category category;

	public ProcessedTransaction(
			@JsonProperty("id") Integer id,
			@JsonProperty("category") Category category,
			@JsonProperty("accountId") Integer accountId,
			@JsonProperty("accountName") String accountName,
			@JsonProperty("transactionUID") String transactionUID,
			@JsonProperty("description") String description,
			@JsonProperty("date") LocalDate date,
			@JsonProperty("flow") TransactionFlow flow,
			@JsonProperty("amount") BigDecimal amount,
			@JsonProperty("status") TransactionStatus status) {
		super(transactionUID, description, date, flow, amount, status);
		this.id = id;
		this.accountId = accountId;
		this.accountName = accountName;
		this.category = category;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
