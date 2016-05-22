package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.dao.bankaccount.TransactionDao;
import com.victormiranda.mani.core.service.user.UserService;
import com.victormiranda.mani.type.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PendingAnalyzer {

    private final TransactionDao transactionDao;
    private final UserService userService;

    @Autowired
    public PendingAnalyzer(TransactionDao transactionDao, UserService userService) {
        this.transactionDao = transactionDao;
        this.userService = userService;
    }

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

    public void promotePendingTransactions(AccountInfo accountInfo, List<Transaction> oldPendings) {
        final List<Transaction> transactionsToPromote = new ArrayList<>(oldPendings);
        //if they are in the input pending transactions, they are not promoted
        transactionsToPromote.removeAll(getNewPendings(accountInfo));

        List<Transaction>  allSyncTransactions = new ArrayList<>(accountInfo.getTransactions());

        allSyncTransactions.addAll(oldPendings);

        for (Transaction candidate : transactionsToPromote) {
            Optional<Transaction> pendingConsolidated = accountInfo.getTransactions().stream()
                    .filter(t -> t.getStatus() == TransactionStatus.NORMAL)
                    .filter(t -> t.getDescription().equals(candidate.getDescription()))
                    .filter(t -> t.getAmount().compareTo(candidate.getAmount()) == 0)
                    .filter(t -> t.getDateAuthorization().compareTo(candidate.getDateAuthorization()) == 0)
                    .findFirst();

            if (pendingConsolidated.isPresent()) {
                allSyncTransactions.remove(pendingConsolidated.get());
                final Integer id = transactionDao.findByUserAndUID(userService.getCurrentUserId().get(), pendingConsolidated.get().getUid()).get().getId();
                allSyncTransactions.add(
                        new Transaction.Builder(pendingConsolidated.get())
                                .withStatus(TransactionStatus.NORMAL)
                                .withId(Optional.of(id))
                                .build());
            } else {
                allSyncTransactions.remove(candidate);
                //add a new version with status pending removed
                allSyncTransactions.add(
                        new Transaction.Builder(candidate)
                                .withStatus(TransactionStatus.PENDING_REMOVED)
                                .build());

            }
        }

        accountInfo.getTransactions().clear();
        accountInfo.getTransactions().addAll(allSyncTransactions);

    }
}
