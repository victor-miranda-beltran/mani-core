package com.victormiranda.mani.core.test.it;

import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.core.dao.bankaccount.BankAccountDao;
import com.victormiranda.mani.core.inputtransformer.impl.ptsb.PTSBInputFlowTransformer;
import com.victormiranda.mani.core.service.category.CategoryService;
import com.victormiranda.mani.type.TransactionFlow;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Optional;

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
    public void testFlowInInternalInTransaction() {
        final Transaction transaction = new Transaction.Builder()
                .withUid("uid")
                .withDescription("INET 24 To 4321")
                .withFlow(TransactionFlow.OUT)
                .build();

        Transaction transformTransaction = inputFlowTransformer.transform(transaction);
        Assert.assertEquals(transformTransaction.getFlow(), TransactionFlow.INNER_OUT);
    }

}
