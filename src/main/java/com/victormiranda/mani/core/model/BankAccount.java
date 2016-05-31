package com.victormiranda.mani.core.model;


import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank_account")
public class BankAccount implements ManiModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="bank_login_id")
	private BankLogin bankLogin;

	@Transient
	private String uuid;
	private String name;
	private String accountNumber;
	private String alias;
	private BigDecimal availableBalance;
	private BigDecimal currentBalance;
	private LocalDate lastSynced;

	@OneToMany(mappedBy = "bankAccount")
	private List<BankTransaction> transactions = new ArrayList<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BankLogin getBankLogin() {
		return bankLogin;
	}

	public void setBankLogin(BankLogin bankLogin) {
		this.bankLogin = bankLogin;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal currentBalance) {
		this.currentBalance = currentBalance;
	}

	public LocalDate getLastSynced() {
		return lastSynced;
	}

	public void setLastSynced(LocalDate lastSynced) {
		this.lastSynced = lastSynced;
	}

	public List<BankTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<BankTransaction> transactions) {
		this.transactions = transactions;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
}
