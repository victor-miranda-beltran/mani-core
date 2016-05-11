package com.victormiranda.mani.core.inputtransformer.impl.ptsb;

import com.victormiranda.mani.bean.Transaction;
import com.victormiranda.mani.core.inputtransformer.InputTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class PTSBInputDateTransformer implements InputTransformer {

    private static final Pattern DESCRIPTION_DATE_PATTERN = Pattern.compile(".*(\\d{2}/\\d{2}).*");
    private static final DateTimeFormatter descriptionFieldDateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final Logger LOGGER = LoggerFactory.getLogger(PTSBInputDateTransformer.class.getName());

    @Override
    public Transaction transform(Transaction input) {
        final String description = input.getDescription();
        final LocalDate localDateFromDate = input.getDate();

        if (description == null || description.trim().length() == 0) {
            return input;
        }

        final Matcher descriptionMatcher = DESCRIPTION_DATE_PATTERN.matcher(description);

        if (!descriptionMatcher.matches()) {
            return input;
        }

        final String dateCleanedFromDescription = descriptionMatcher.group(1) + "/" + localDateFromDate.getYear();
        final LocalDate localDateFromDesc;

        try {
            localDateFromDesc = LocalDate.parse(dateCleanedFromDescription, descriptionFieldDateFormatter);
        } catch (Exception e) {
            LOGGER.warn("Date found with value " + dateCleanedFromDescription + ", returning original value");
            return input;
        }

        final long differenceBetweenDates = ChronoUnit.DAYS.between(localDateFromDesc, localDateFromDate);

        if (differenceBetweenDates > 0 && differenceBetweenDates < 6) {
            return new Transaction.Builder(input).withDate(localDateFromDesc).build();
        }

        return input;
    }
}
