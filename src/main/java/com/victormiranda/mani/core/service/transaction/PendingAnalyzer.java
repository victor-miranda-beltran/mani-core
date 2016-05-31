package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.dao.bankaccount.TransactionDao;
import com.victormiranda.mani.core.model.BankTransaction;
import com.victormiranda.mani.core.service.synchronization.SyncBatch;
import com.victormiranda.mani.core.service.user.UserService;
import com.victormiranda.mani.type.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PendingAnalyzer {

    public SyncBatch processSyncAccountResult(final AccountInfo accountInfo,
                                              final List<Transaction> knownPendingTransactions,
                                              final List<Transaction> settledTransactions) {
        final SyncBatch syncBatch = new SyncBatch(accountInfo.getId());

        for (Transaction transaction : accountInfo.getTransactions()) {
            if (transaction.getStatus() == TransactionStatus.PENDING) {
                processPendingTransaction(transaction, knownPendingTransactions, syncBatch);
            } else {
                processSettledTransaction(transaction, knownPendingTransactions, settledTransactions, syncBatch);
            }
        }

        for (Transaction deleteCandidate : knownPendingTransactions) {
            //check if now is still pending or has been promoted to settled
            final boolean removed = accountInfo.getTransactions().stream()
                    .filter(t -> pendingComparator(t, deleteCandidate)).count() == 0;

            if (removed) {
                syncBatch.getDeletedTransactions().add(deleteCandidate);
            }
        }

        return syncBatch;
    }

    private void processSettledTransaction(Transaction transaction, List<Transaction> knownPendingTransactions, List<Transaction> settledTransactions, SyncBatch syncBatch) {
        final boolean wasSettledBefore = settledTransactions.stream().filter(t -> t.getUid().equals(transaction.getUid())).count() != 0;

        if (!wasSettledBefore) {
            final boolean wasPendingBefore = knownPendingTransactions.stream()
                    .filter(t-> pendingComparator( t, transaction)).count() != 0;
            if (wasPendingBefore) {
                syncBatch.getUpdatedTransactions().add(transaction);
            } else {
                syncBatch.getNewTransactions().add(transaction);
            }
        }
    }

    private void processPendingTransaction(
            final Transaction transaction,
            final  List<Transaction> knownPendingTransactions,
            final SyncBatch syncBatch) {

        final boolean isKnownPending = knownPendingTransactions.stream()
                .filter(t -> pendingComparator(transaction, t))
                .count() != 0;
        if (!isKnownPending) {
            syncBatch.getNewTransactions().add(transaction);
        }
    }

    private boolean pendingComparator(Transaction transaction, Transaction t) {
        return t.getDescription().equals(transaction.getDescription())
                && t.getAmount().compareTo(transaction.getAmount()) == 0
                && t.getFlow() == transaction.getFlow()
                && t.getDateAuthorization().compareTo(transaction.getDateAuthorization()) == 0;
    }


}


