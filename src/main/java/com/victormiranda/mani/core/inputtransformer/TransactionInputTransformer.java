package com.victormiranda.mani.core.inputtransformer;

import com.victormiranda.mani.bean.Transaction;

public interface TransactionInputTransformer extends InputTransformer {

    Transaction transformTransaction(final Transaction input);
}
