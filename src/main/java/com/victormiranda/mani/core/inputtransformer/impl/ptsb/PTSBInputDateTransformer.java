package com.victormiranda.mani.core.inputtransformer.impl.ptsb;

import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.inputtransformer.InputTransformer;
import org.springframework.stereotype.Component;

@Component
public class PTSBInputDateTransformer implements InputTransformer {

    @Override
    public Transaction transform(Transaction input) {
        return input;
    }
}
