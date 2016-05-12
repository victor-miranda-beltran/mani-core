package com.victormiranda.mani.core.test.it.transaction;

import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.service.transaction.TransactionServiceImpl;
import com.victormiranda.mani.core.test.it.BaseIT;
import com.victormiranda.mani.core.test.it.ITApp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringApplicationConfiguration(classes = ITApp.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class TransactionServiceImplIT extends BaseIT {


    @Autowired
    private TransactionServiceImpl transactionService;

    @Test
    @Transactional
    public void testProcessTransaction() {

        final Transaction transaction = new Transaction.Builder()
                .withId(Optional.of(new Random().nextInt()))
                .withAccount(Optional.of(CURRENT_TEST_ACCOUNT))
                .withDescription("new transact")
                .withUid("uiid")
                .withDate(LocalDate.now())
                .withAmount(new BigDecimal(100))
                .build();

        final int sizePreInsert = transactionService.getTransactions().size();

        transactionService.processTransaction(transaction);

        final int sizePostInsert = transactionService.getTransactions().size();

        Assert.assertEquals(sizePreInsert + 1 , sizePostInsert);

    }
}
