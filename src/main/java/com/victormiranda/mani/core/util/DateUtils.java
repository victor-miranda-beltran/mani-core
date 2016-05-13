package com.victormiranda.mani.core.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class DateUtils {

    public static List<LocalDate> getDateRange(final LocalDate start, final LocalDate end) {
        return Stream.iterate(
                start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .collect(Collectors.toList());
    }
}
