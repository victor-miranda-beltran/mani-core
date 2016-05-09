package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.Transaction;

@FunctionalInterface
public interface TransactionTransformer {

    Transaction transform(final Transaction transaction);


}
