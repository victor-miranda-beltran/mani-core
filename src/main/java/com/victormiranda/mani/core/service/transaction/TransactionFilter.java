package com.victormiranda.mani.core.service.transaction;

import com.victormiranda.mani.bean.category.Category;
import com.victormiranda.mani.type.TransactionFlow;

import java.time.LocalDate;

public class TransactionFilter {

    private final TransactionFlow flow;
    private final LocalDate start;
    private final LocalDate end;
    private final Category category;

    public TransactionFilter(final Builder builder) {
        this.start = builder.start;
        this.end = builder.end;
        this.category = builder.category;
        this.flow = builder.flow;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public Category getCategory() {
        return category;
    }

    public TransactionFlow getFlow() {
        return flow;
    }

    public static class Builder {

        LocalDate start;
        LocalDate end;
        Category category;
        public TransactionFlow flow;

        public Builder withStart(final LocalDate val) {
            this.start = val;
            return this;
        }

        public Builder withEnd(final LocalDate val) {
            this.start = val;
            return this;
        }

        public Builder withCategory(final Category val) {
            this.category = val;
            return this;
        }

        public Builder withFlow(final TransactionFlow val) {
            this.flow = val;
            return this;
        }

        public TransactionFilter build() {
            return new TransactionFilter(this);
        }
    }
}
