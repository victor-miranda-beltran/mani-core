package com.victormiranda.mani.core.inputtransformer.impl.ptsb;


import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.inputtransformer.InputTransformer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PTSBInputDescriptionTransformer implements InputTransformer {

    final List<Pattern> patterns;

    public PTSBInputDescriptionTransformer() {
        patterns = new ArrayList<>();

        patterns.add(Pattern.compile("^POS "));
        patterns.add(Pattern.compile("^CNC "));
        patterns.add(Pattern.compile("\\d{2}/\\d{2} "));
        patterns.add(Pattern.compile(" \\d{1}$"));
        patterns.add(Pattern.compile("\\d{2}:\\d{2}$"));
    }

    @Override
    public Transaction transform(final Transaction transaction) {

        String descriptionProcessed =  transaction.getDescription();

        for (Pattern pattern : patterns) {
            descriptionProcessed = descriptionProcessed.replaceAll(pattern.pattern(),"");
        }

        return new Transaction.Builder(transaction)
                .withDescriptionProcessed(descriptionProcessed)
                .build();
    }
}
