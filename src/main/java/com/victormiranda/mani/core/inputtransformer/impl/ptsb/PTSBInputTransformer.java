package com.victormiranda.mani.core.inputtransformer.impl.ptsb;


import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.inputtransformer.InputTransformer;
import com.victormiranda.mani.core.inputtransformer.impl.InputCategoryTransformer;
import com.victormiranda.mani.core.service.transaction.TransactionTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class PTSBInputTransformer implements TransactionTransformer {

    private Set<InputTransformer> processingQueue = new LinkedHashSet<>();

    @Autowired
    public PTSBInputTransformer(final PTSBInputDescriptionTransformer inputDescriptionTransformer,
                                final PTSBInputDateTransformer inputDateTransformer,
                                final InputCategoryTransformer inputCategoryTransformer,
                                final PTSBInputFlowTransformer inputFlowTransformer) {
        processingQueue.add(inputDescriptionTransformer);
        processingQueue.add(inputDateTransformer);
        processingQueue.add(inputFlowTransformer);
        processingQueue.add(inputCategoryTransformer);
    }

    @Override
    public Transaction transform(final Transaction transaction) {

        Transaction transactionModified = transaction;

        for (InputTransformer inputTransformer : processingQueue) {
            transactionModified = inputTransformer.transform(transactionModified);
        }

        return transactionModified;
    }
}
