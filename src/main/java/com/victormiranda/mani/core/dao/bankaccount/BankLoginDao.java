package com.victormiranda.mani.core.dao.bankaccount;

import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.model.BankLogin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankLoginDao extends CrudRepository<BankLogin, Integer>{

}
