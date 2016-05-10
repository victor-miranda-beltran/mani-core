package com.victormiranda.mani.core.inputtransformer;

import com.victormiranda.mani.bean.Transaction;

public interface InputTransformer {

    Transaction transform(final Transaction input);

}
