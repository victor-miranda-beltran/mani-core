package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.model.BankTransaction;
import com.victormiranda.mani.type.TransactionStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PendingAnalyzer {

    public List<Transaction> getPendingsToStore(final AccountInfo accountInfo, List<Transaction> oldPendings) {
        return getNewPendings(accountInfo).stream()
                .filter(t -> !isExistingPending(t, oldPendings))
                .collect(Collectors.toList());
    }

    private boolean isExistingPending(final Transaction candidate, final List<Transaction> oldPendings) {
        return oldPendings.stream()
                .filter(t -> t.getDescription().equals(candidate.getDescription()))
                .filter(t -> t.getAmount().compareTo(candidate.getAmount()) == 0)
                .count() > 0;
    }

    private List<Transaction> getNewPendings(AccountInfo accountInfo) {
        return accountInfo.getTransactions().stream()
                .filter(t -> t.getStatus() == TransactionStatus.PENDING)
                .collect(Collectors.toList());
    }

    public void fetchPendingPromotions(AccountInfo accountInfo, List<Transaction> oldPendings) {
        final List<Transaction> transactionsToPromote = new ArrayList<>(oldPendings);
        //if they are in the input pending transactions, they are not promoted
        transactionsToPromote.removeAll(getNewPendings(accountInfo));

        List<Transaction>  allSyncTransactions = new ArrayList<>(accountInfo.getTransactions());

        for (Transaction candidate : transactionsToPromote) {
            Optional<Transaction> pendingConsolidated = accountInfo.getTransactions().stream()
                    .filter(t -> t.getStatus() == TransactionStatus.NORMAL)
                    .filter(t -> t.getDescription().equals(candidate.getDescription()))
                    .filter(t -> t.getAmount().compareTo(candidate.getAmount()) == 0)
                    .filter(t -> t.getDateAuthorization().compareTo(candidate.getDateAuthorization()) == 0)
                    .findFirst();

            if (pendingConsolidated.isPresent()) {
                // remove from allSyncTransactions
                allSyncTransactions.remove(pendingConsolidated.get());
            } else {

            }
        }

        accountInfo.getTransactions().clear();
        accountInfo.getTransactions().addAll(allSyncTransactions);

    }
}
