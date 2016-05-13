package com.victormiranda.mani.core.test.ut.inputtransformer.impl.ptsb;

import com.victormiranda.mani.bean.BaseAccountInfo;
import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.inputtransformer.impl.ptsb.PTSBInputDescriptionTransformer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class PTSBInputDescriptionTransformerTest {

    private PTSBInputDescriptionTransformer inputDescriptionTransformer;

    @Before
    public void setUp() throws Exception {
        inputDescriptionTransformer = new PTSBInputDescriptionTransformer();
    }

    @Test
    public void testTransformCleanPrefix() throws Exception {

        final Transaction transaction = new Transaction.Builder()
                .withUid("ozu")
                .withDate(LocalDate.now())
                .withDescription("POS CTC")
                .withAccount(new BaseAccountInfo(1,"demo", "123"))
                .build();

        final Transaction transformedTrans = inputDescriptionTransformer.transform(transaction);

        Assert.assertEquals(transformedTrans.getDescriptionProcessed(), "CTC");
    }

    @Test
    public void testTransform() throws Exception {

        final Transaction transaction = new Transaction.Builder()
                .withUid("ozu")
                .withDate(LocalDate.now())
                .withAccount(new BaseAccountInfo(1,"demo", "123"))
                .withDescription("Transaction with date 19/02")
                .build();

        final Transaction transformedTrans = inputDescriptionTransformer.transform(transaction);

        Assert.assertEquals(transformedTrans.getDescriptionProcessed(), "Transaction with date ");
    }
}