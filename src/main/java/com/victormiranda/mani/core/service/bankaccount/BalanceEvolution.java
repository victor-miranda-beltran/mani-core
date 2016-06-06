package com.victormiranda.mani.core.service.bankaccount;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.victormiranda.mani.bean.AccountInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown=true)
public final class BalanceEvolution {

    private final AccountInfo accountInfo;

    private final List<DayBalance> dayBalance = new ArrayList<>();

    public BalanceEvolution(AccountInfo accountInfo, LinkedHashMap<LocalDate, BigDecimal> dayBalance) {
        this.accountInfo = accountInfo;
        this.dayBalance.addAll(
                dayBalance.entrySet().stream()
                        .map(day -> new DayBalance(day.getKey(), day.getValue()))
                        .collect(Collectors.toList()));
    }

    public AccountInfo getAccountInfo() {
        return accountInfo;
    }

    public List<DayBalance> getDayBalance() {
        return dayBalance;
    }
}

class DayBalance {
    private final LocalDate day;
    private final BigDecimal balance;

    DayBalance(LocalDate day, BigDecimal balance) {
        this.day = day;
        this.balance = balance;
    }

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="UTC")
    public LocalDate getDay() {
        return day;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
