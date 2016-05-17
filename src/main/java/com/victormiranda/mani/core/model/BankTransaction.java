package com.victormiranda.mani.core.model;


import com.victormiranda.mani.type.TransactionFlow;
import com.victormiranda.mani.type.TransactionStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
public class BankTransaction implements ManiModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String uid;

	private String descriptionOriginal;

	private String descriptionProcessed;

	private LocalDate dateAuthorization;

	private LocalDate dateSettled;

	private LocalDate dateProcessed;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private TransactionCategory category;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "flow")
	private TransactionFlow flow;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "status")
	private TransactionStatus transactionStatus;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bank_account_id")
	private BankAccount bankAccount;

	private BigDecimal amount;

	private BigDecimal balance;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDescriptionOriginal() {
		return descriptionOriginal;
	}

	public void setDescriptionOriginal(String descriptionOriginal) {
		this.descriptionOriginal = descriptionOriginal;
	}

	public String getDescriptionProcessed() {
		return descriptionProcessed;
	}

	public void setDescriptionProcessed(String descriptionProcessed) {
		this.descriptionProcessed = descriptionProcessed;
	}

	public LocalDate getDateAuthorization() {
		return dateAuthorization;
	}

	public void setDateAuthorization(LocalDate date) {
		this.dateAuthorization = date;
	}

	public LocalDate getDateSettled() {
		return dateSettled;
	}

	public void setDateSettled(LocalDate dateProccesed) {
		this.dateSettled = dateProccesed;
	}

	public LocalDate getDateProcessed() {
		return dateProcessed;
	}

	public void setDateProcessed(LocalDate dateProcessed) {
		this.dateProcessed = dateProcessed;
	}

	public TransactionCategory getCategory() {
		return category;
	}

	public void setCategory(TransactionCategory category) {
		this.category = category;
	}

	public TransactionFlow getFlow() {
		return flow;
	}

	public void setFlow(TransactionFlow flow) {
		this.flow = flow;
	}

	public TransactionStatus getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(TransactionStatus transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BankAccount getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}
}
