package com.victormiranda.mani.core.controller;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.core.service.bankaccount.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class AccountController {

    private final BankAccountService bankAccountService;

    @Autowired
    public AccountController(final BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @RequestMapping("/accounts")
    public Set<AccountInfo> getAccounts() {
        return bankAccountService.getAccountsInfo();
    }
}
