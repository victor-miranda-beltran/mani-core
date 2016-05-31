package com.victormiranda.mani.core.test.ut.syncronization;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.service.synchronization.SyncBatch;
import com.victormiranda.mani.core.service.transaction.PendingAnalyzer;
import com.victormiranda.mani.type.TransactionStatus;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PendingAnalyzerTest {

    private PendingAnalyzer pendingAnalyzer;

    @Before
    public void setUp() {
        pendingAnalyzer = new PendingAnalyzer();
    }

    @Test
    public void testProcessWithNewSettled() throws Exception {
        final AccountInfo accountInfo = getTestAccount();

        accountInfo.getTransactions().add(
                new Transaction.Builder()
                        .withStatus(TransactionStatus.NORMAL)
                        .withUid("1234")
                        .withDescription("new settled")
                        .withAccount(accountInfo)
                .build()
        );
        final List<Transaction> knownPendings = new ArrayList<>();
        final List<Transaction> settledTransactions = new ArrayList<>();

        final SyncBatch syncBatch = pendingAnalyzer.processSyncAccountResult(accountInfo, knownPendings, settledTransactions);

        assertEquals(1, syncBatch.getNewTransactions().size());
    }

    @Test
    public void testProcessWithNewPending() throws Exception {
        final AccountInfo accountInfo = getTestAccount();

        accountInfo.getTransactions().add(
                new Transaction.Builder()
                        .withStatus(TransactionStatus.PENDING)
                        .withUid("1234")
                        .withDescription("new pending")
                        .withAccount(accountInfo)
                        .build()
        );
        final List<Transaction> knownPendings = new ArrayList<>();
        final List<Transaction> settledTransactions = new ArrayList<>();

        final SyncBatch syncBatch = pendingAnalyzer.processSyncAccountResult(accountInfo, knownPendings, settledTransactions);

        assertEquals(1, syncBatch.getNewTransactions().size());
    }

    @Test
    public void testProcessWithExistingSettled() throws Exception {
        final AccountInfo accountInfo = getTestAccount();

        final Transaction settledTransaction =  new Transaction.Builder()
                .withStatus(TransactionStatus.NORMAL)
                .withUid("1234")
                .withDescription("new pending")
                .withAccount(accountInfo)
                .build();

        accountInfo.getTransactions().add(settledTransaction);

        final List<Transaction> knownPendings = new ArrayList<>();
        final List<Transaction> settledTransactions = new ArrayList<>();

        settledTransactions.add(settledTransaction);

        SyncBatch syncBatch = pendingAnalyzer.processSyncAccountResult(accountInfo, knownPendings, settledTransactions);
        assertEquals(0, syncBatch.getNewTransactions().size());
    }

    @Test
    public void testProcessWithExistingSettledAsPending() throws Exception {
        final AccountInfo accountInfo = getTestAccount();

        final Transaction pendingTransaction =  new Transaction.Builder()
                .withStatus(TransactionStatus.PENDING)
                .withUid("1234")
                .withDateAuthorization(LocalDate.now())
                .withDescription("new pending")
                .withAmount(BigDecimal.TEN)
                .withAccount(accountInfo)
                .build();

        final Transaction settledTransaction =  new Transaction.Builder()
                .withStatus(TransactionStatus.NORMAL)
                .withUid("1234")
                .withDateAuthorization(LocalDate.now())
                .withAmount(BigDecimal.TEN)
                .withDescription("new pending")
                .withAccount(accountInfo)
                .build();

        accountInfo.getTransactions().add(settledTransaction);

        final List<Transaction> settledTransactions = new ArrayList<>();

        final List<Transaction> knownPendings = new ArrayList<>();
        knownPendings.add(pendingTransaction);

        SyncBatch syncBatch = pendingAnalyzer.processSyncAccountResult(accountInfo, knownPendings, settledTransactions);
        assertEquals(1, syncBatch.getUpdatedTransactions().size());
    }

    @Test
    public void testOldPendingDissapeared() throws Exception {
        final AccountInfo accountInfo = getTestAccount();

        final Transaction oldPendingTransaction =  new Transaction.Builder()
                .withStatus(TransactionStatus.PENDING)
                .withUid("1234")
                .withDateAuthorization(LocalDate.now())
                .withDescription("new pending")
                .withAmount(BigDecimal.TEN)
                .withAccount(accountInfo)
                .build();

        final List<Transaction> settledTransactions = new ArrayList<>();

        final List<Transaction> knownPendings = new ArrayList<>();
        knownPendings.add(oldPendingTransaction);

        SyncBatch syncBatch = pendingAnalyzer.processSyncAccountResult(accountInfo, knownPendings, settledTransactions);
        assertEquals(1, syncBatch.getDeletedTransactions().size());
    }

    private AccountInfo getTestAccount() {
        return new AccountInfo.Builder()
                .withId(1)
                .withAccountNumber("1234")
                .withName("testSynchronizeWithNewPendings")
                .build();
    }
}