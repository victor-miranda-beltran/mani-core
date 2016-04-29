package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.dao.bankaccount.TransactionDao;
import com.victormiranda.mani.core.model.BankLogin;
import com.victormiranda.mani.core.model.TransactionModel;
import com.victormiranda.mani.core.service.bankaccount.BankAccountService;
import com.victormiranda.mani.core.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

	private final UserService userService;
	private final BankAccountService bankAccountService;
	private final TransactionDao transactionDao;

	@Autowired
	public TransactionServiceImpl(UserService userService, BankAccountService bankAccountService, TransactionDao transactionDao) {
		this.userService = userService;
		this.bankAccountService = bankAccountService;
		this.transactionDao = transactionDao;
	}


	private Transaction  toTransaction(TransactionModel tm) {
		return  new Transaction(tm.getUid(), tm.getDescriptionOriginal(), tm.getDate(),
								tm.getFlow(), tm.getAmount(), tm.getTransactionStatus());
	}
}
