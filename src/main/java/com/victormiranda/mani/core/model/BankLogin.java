package com.victormiranda.mani.core.model;


import com.victormiranda.mani.type.BankProvider;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bank_login")
public class BankLogin implements ManiModel {

	@Id
	private Integer id;

	@Enumerated(value = EnumType.STRING)
	@Column(name = "bank_provider")
	private BankProvider bankProvider;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	private String field1;
	private String field2;
	private String field3;
	private String field4;

	@OneToMany(mappedBy="bankLogin")
	private Set<BankAccount> bankAccounts =  new HashSet<>();

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public Set<BankAccount> getBankAccounts() {
		return bankAccounts;
	}

	public void setBankAccounts(Set<BankAccount> bankAccounts) {
		this.bankAccounts = bankAccounts;
	}
}
