package com.victormiranda.mani.core.dao.bankaccount;

import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BankAccountDao extends CrudRepository<BankAccount, Integer> {

	@Query("select b from BankAccount b where b.bankLogin.user.id  = ?1 and b.accountNumber = ?2")
	public Optional<BankAccount> fetchAccount(final Integer userId, final String accountNumber);

	@Query("select b from BankAccount b where b.bankLogin.user.id  = ?1")
	public Set<BankAccount> fetchAccounts(final Integer userId);

}
