package com.victormiranda.mani.core.test.it.synchronization;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.SynchronizationResult;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.model.BankLogin;
import com.victormiranda.mani.core.service.bankaccount.BankAccountService;
import com.victormiranda.mani.core.service.transaction.TransactionService;
import com.victormiranda.mani.core.test.it.BaseIT;
import com.victormiranda.mani.core.test.it.ITApp;
import com.victormiranda.mani.type.TransactionFlow;
import com.victormiranda.mani.type.TransactionStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringApplicationConfiguration(classes = ITApp.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@Transactional
public class SynchronizationServiceIT  extends BaseIT {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BankAccountService bankAccountService;

    @Test
    public void testReadPendings() {
        final AccountInfo accountInfo = bankAccountService.getAccountById(1);

        final List<Transaction> pendingBankTransactionsAfterFirstSync =
                transactionService.getPendingTransactionsFromBankAccount(accountInfo.getId());

        Assert.assertEquals(1, pendingBankTransactionsAfterFirstSync.size());
    }

    @Test
    public void testSynchronizeWithNewPendings() {
        final AccountInfo testAccount = getTestAccount();

        final List<Transaction> transactionsIncluded = new ArrayList<>();

        final Transaction newPendingTransaction = new Transaction.Builder()
                .withAccount(testAccount)
                .withDescription("new sync pending")
                .withAmount(BigDecimal.ONE)
                .withStatus(TransactionStatus.PENDING)
                .build();

        final Transaction oldPendingTransactionThatStillExists = new Transaction.Builder()
                .withAccount(testAccount)
                .withId(Optional.of(2))
                .withUid("transuid2")
                .withDescription("POS Amazon.co.uk 01/12")
                .withFlow(TransactionFlow.OUT)
                .withAmount(BigDecimal.TEN)
                .withDateAuthorization(LocalDate.of(2016,12,1))
                .withStatus(TransactionStatus.PENDING)
                .build();

        transactionsIncluded.add(newPendingTransaction);
        transactionsIncluded.add(oldPendingTransactionThatStillExists);

        executeSync(transactionsIncluded);

        final List<Transaction> pendingBankTransactionsAfterFirstSync =
                transactionService.getPendingTransactionsFromBankAccount(1);

        Assert.assertEquals(2, pendingBankTransactionsAfterFirstSync.size());
    }

    @Test
    public void testSynchronizeWithExistingPendings() {
        final AccountInfo testAccount = getTestAccount();

        final List<Transaction> transactionsIncluded = new ArrayList<>();

        final Transaction existingPendingTransaction = new Transaction.Builder()
                .withAccount(testAccount)
                .withDescription("POS Amazon.co.uk 01/12")
                .withDateAuthorization(LocalDate.of(2016,1,12))
                .withAmount(BigDecimal.TEN)
                .withStatus(TransactionStatus.PENDING)
                .build();

        transactionsIncluded.add(existingPendingTransaction);

        executeSync(transactionsIncluded);

        final List<Transaction> pendingBankTransactionsAfterFirstSync =
                transactionService.getPendingTransactionsFromBankAccount(1);

        //the number of pendings transactions is not increased - transaction has been ignored as it should be
        Assert.assertEquals(1, pendingBankTransactionsAfterFirstSync.size());
    }

    @Test
    public void testSynchronizeWithExistingPendingRemoved() {
        final List<Transaction> transactionsIncluded = new ArrayList<>();

        executeSync(transactionsIncluded);

        final List<Transaction> pendingBankTransactionsAfterFirstSync =
                transactionService.getPendingTransactionsFromBankAccount(1);

        //after this synchronization, the pending transaction should be removed
        Assert.assertEquals(0, pendingBankTransactionsAfterFirstSync.size());
    }

    @Test
    public void testSynchronizeWithExistingPendingPromoted() {
        final List<Transaction> transactionsIncluded = new ArrayList<>();

        transactionsIncluded.add(
                new Transaction.Builder()
                    .withAccount(getTestAccount())
                        .withUid("transuid2")
                        .withAmount(BigDecimal.TEN)
                        .withStatus(TransactionStatus.NORMAL)
                        .withDescription("POS Amazon.co.uk 01/12")
                        .withDateAuthorization(LocalDate.of(2016,12,1))
                        .build());

        executeSync(transactionsIncluded);

        final List<Transaction> pendingBankTransactionsAfterFirstSync =
                transactionService.getPendingTransactionsFromBankAccount(1);

        //after this synchronization, the pending transaction should be removed
        Assert.assertEquals(0, pendingBankTransactionsAfterFirstSync.size());


        //after this synchronization, the old pending should be a regular transaction
        Assert.assertEquals(0, pendingBankTransactionsAfterFirstSync.size());
    }

    private List<BankAccount> executeSync(List<Transaction> transactions) {
        final Set<AccountInfo> accounts = new HashSet<>();

        final AccountInfo testAccount = getTestAccount();

        accounts.add(testAccount);

        testAccount.getTransactions().addAll(transactions);

        final SynchronizationResult synchronizationResult = new SynchronizationResult(accounts, true);

        final BankLogin bankLogin = new BankLogin();
        bankLogin.setId(1);

        return bankAccountService.syncBankAccounts(bankLogin, synchronizationResult);
    }


    public AccountInfo getTestAccount() {
        return new AccountInfo.Builder()
                .withId(1)
                .withAccountNumber("1234")
                .withName("testSynchronizeWithNewPendings")
                .build();
    }
}
