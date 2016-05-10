package com.victormiranda.mani.core.inputtransformer.impl;


import com.victormiranda.mani.bean.AccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.dao.bankaccount.BankAccountDao;
import com.victormiranda.mani.core.inputtransformer.InputTransformer;
import com.victormiranda.mani.core.model.BankAccount;
import com.victormiranda.mani.core.service.bankaccount.BankAccountService;
import com.victormiranda.mani.core.service.transaction.TransactionServiceImpl;
import com.victormiranda.mani.core.service.user.UserService;
import com.victormiranda.mani.type.TransactionFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.Regexp;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PTSBInputFlowTransformer implements InputTransformer {

    private final BankAccountDao bankAccountDao;
    private final UserService userService;

    private final static String TRANSFER_IN = "INET 24 Fr \\d{4}";
    private final static String TRANSFER_TO = "INET 24 To \\d{4}";
    private final static Pattern TRANSFER = Pattern.compile("INET 24 (?:Fr|To) (\\d{4})");

    @Autowired
    public PTSBInputFlowTransformer(final BankAccountDao bankAccountDao, final UserService userService) {
        this.bankAccountDao = bankAccountDao;
        this.userService = userService;
    }

    public Transaction transform(final Transaction transaction) {
        final TransactionFlow calculatedFlow;
        final String modifiedDescription;
        final boolean isTransferIn = Pattern.matches(TRANSFER_IN, transaction.getDescription());
        final boolean isTransferOut = Pattern.matches(TRANSFER_TO, transaction.getDescription());

        final Optional<BankAccount> destinationAccount =
                isTransferIn || isTransferOut ? getDestination(transaction) : Optional.empty();

        final boolean isInnerTransaction =
                (isTransferIn || isTransferOut) && destinationAccount.isPresent();

        if (!isInnerTransaction) {
            return transaction;
        } else if (isTransferIn) {
            calculatedFlow = TransactionFlow.INNER_IN;
            modifiedDescription = "Transfer to " + destinationAccount.get().getAlias();
        } else  {
            calculatedFlow = TransactionFlow.INNER_OUT;
            modifiedDescription = "Transfer from " + destinationAccount.get().getAlias();
        }

        return new Transaction.Builder(transaction)
                .withFlow(calculatedFlow)
                .withDescriptionProcessed(modifiedDescription)
                .build();
    }


    private Optional<BankAccount> getDestination(final Transaction transaction) {
        final Matcher matcher = TRANSFER.matcher(transaction.getDescription());

        if (matcher.find()) {
            final String destinationAccount = matcher.group(1);

            final Integer userId = userService.getCurrentUserId().get();
            return bankAccountDao.fetchAccounts(userId).stream()
                    .filter(a -> destinationAccount.equals(a.getAccountNumber()))
                    .findFirst();
        } else {
            return Optional.empty();
        }

    }
}
