package com.victormiranda.mani.core.test.ut.inputtransformer.impl.ptsb;

import com.victormiranda.mani.core.converter.LocalDateAttributeConverter;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Date;
import java.time.LocalDate;

public class LocalDateAttConverterTest {

    @Test
    public void testConversionInTwoDirections() {
        final LocalDate localDateNow = LocalDate.now();

        final LocalDateAttributeConverter converter = new LocalDateAttributeConverter();
        final Date dateFromLocalDate = converter.convertToDatabaseColumn(localDateNow);

        final LocalDate localDateAfterConversions = converter.convertToEntityAttribute(dateFromLocalDate);

        Assert.assertEquals(localDateNow, localDateAfterConversions);
    }
}
