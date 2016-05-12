package com.victormiranda.mani.core.test.inputtransformer.impl.ptsb;

import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.inputtransformer.impl.ptsb.PTSBInputDateTransformer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InputDateTransformerTest {

    private PTSBInputDateTransformer inputDateTransformer;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");

    @Before
    public void setup() {
        this.inputDateTransformer = new PTSBInputDateTransformer();
    }

    @Test
    public void testProcessTransactionWithoutDateInDescription() {

        final Transaction transaction = new Transaction.Builder()
                .withUid("ozu")
                .withDate(LocalDate.now())
                .withDescription("Random")
                .build();

        final Transaction transformedTransaction = inputDateTransformer.transform(transaction);

        Assert.assertEquals(transformedTransaction.getDate(), LocalDate.now());
    }

    @Test
    public void testProcessTransactionWithEmptyDescription() {

        final Transaction transaction = new Transaction.Builder()
                .withUid("ozu")
                .withDate(LocalDate.now())
                .withDescription("")
                .build();

        Transaction transformedTransaction = inputDateTransformer.transform(transaction);

        Assert.assertEquals(transformedTransaction.getDate(), LocalDate.now());
    }

    @Test
    public void testProcessTransactionWithoutWrongDateInDescription() {

        final Transaction transaction = new Transaction.Builder()
                .withUid("ozu")
                .withDate(LocalDate.now())
                .withDescription("Random 19/19")
                .build();

        final Transaction transformedTransaction = inputDateTransformer.transform(transaction);

        Assert.assertEquals(transformedTransaction.getDate(), LocalDate.now());
    }

    @Test
    public void testProcessTransactionWithOldDateInDescription() {
        final LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);

        final Transaction transaction = new Transaction.Builder()
                .withUid("ozu")
                .withDate(LocalDate.now())
                .withDescription("Random " + formatter.format(oneMonthAgo))
                .build();

        final Transaction transformedTransaction = inputDateTransformer.transform(transaction);

        Assert.assertEquals(transformedTransaction.getDate(), LocalDate.now());
    }

    @Test
    public void testProcessTransactionWithDateInDescription() {
        final LocalDate threeDaysAgo = LocalDate.now().minusDays(3);

        final Transaction transaction = new Transaction.Builder()
                .withUid("ozu")
                .withDate(LocalDate.now())
                .withDescription(formatter.format(threeDaysAgo))
                .build();

        final Transaction transformedTransaction = inputDateTransformer.transform(transaction);

        Assert.assertEquals(transformedTransaction.getDate(), threeDaysAgo);
    }

    @Test
    public void testProcessTransactionWithFutureDateInDescription() {
        final LocalDate threeDaysInTheFuture = LocalDate.now().plusDays(3);

        final Transaction transaction = new Transaction.Builder()
                .withUid("ozu")
                .withDate(LocalDate.now())
                .withDescription(formatter.format(threeDaysInTheFuture))
                .build();

        Transaction transformedTransaction = inputDateTransformer.transform(transaction);

        Assert.assertEquals(transformedTransaction.getDate(), LocalDate.now());
    }
}
