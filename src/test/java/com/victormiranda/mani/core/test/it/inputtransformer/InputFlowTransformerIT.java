package com.victormiranda.mani.core.test.it.inputtransformer;

import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.inputtransformer.impl.ptsb.PTSBInputFlowTransformer;
import com.victormiranda.mani.core.test.it.BaseIT;
import com.victormiranda.mani.core.test.it.ITApp;
import com.victormiranda.mani.type.TransactionFlow;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringApplicationConfiguration(classes = ITApp.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class InputFlowTransformerIT extends BaseIT {

    @Autowired
    private PTSBInputFlowTransformer inputFlowTransformer;

    @Test
    public void testFlowInUnrelatedTransaction() {

        final Transaction transaction = new Transaction.Builder()
                .withUid("uid")
                .withDescription("ozu")
                .withFlow(TransactionFlow.OUT)
                .build();

        Transaction transformTransaction = inputFlowTransformer.transform(transaction);
        Assert.assertEquals(transformTransaction.getFlow(), transaction.getFlow());
    }

    @Test
    public void testFlowInInternalOutTransaction() {
        final Transaction transaction = new Transaction.Builder()
                .withUid("uid")
                .withDescription("INET 24 To 4321")
                .withFlow(TransactionFlow.OUT)
                .build();

        Transaction transformTransaction = inputFlowTransformer.transform(transaction);
        Assert.assertEquals(transformTransaction.getFlow(), TransactionFlow.INNER_OUT);
    }

    @Test
    public void testFlowInInternalInTransaction() {
        final Transaction transaction = new Transaction.Builder()
                .withUid("uid")
                .withDescription("INET 24 Fr 1234")
                .withFlow(TransactionFlow.IN)
                .build();

        Transaction transformTransaction = inputFlowTransformer.transform(transaction);
        Assert.assertEquals(transformTransaction.getFlow(), TransactionFlow.INNER_IN);
    }

    @Test
    public void testFlowInExternalInTransaction() {
        final Transaction transaction = new Transaction.Builder()
                .withUid("uid")
                .withDescription("INET 24 Fr 4544")
                .withFlow(TransactionFlow.IN)
                .build();

        Transaction transformTransaction = inputFlowTransformer.transform(transaction);
        Assert.assertEquals(transformTransaction.getFlow(), TransactionFlow.IN);
    }

}
