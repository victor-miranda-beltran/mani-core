package com.victormiranda.mani.core.service.synchronization;

import com.victormiranda.mani.bean.Transaction;

import java.util.ArrayList;
import java.util.List;

public class SyncBatch {
    private final Integer accountId;

    private List<Transaction> newTransactions = new ArrayList<>();
    private List<Transaction> updatedTransactions = new ArrayList<>();
    private List<Transaction> deletedTransactions = new ArrayList<>();

    public SyncBatch(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public List<Transaction> getNewTransactions() {
        return newTransactions;
    }

    public void setNewTransactions(List<Transaction> newTransactions) {
        this.newTransactions = newTransactions;
    }

    public List<Transaction> getUpdatedTransactions() {
        return updatedTransactions;
    }

    public void setUpdatedTransactions(List<Transaction> updatedTransactions) {
        this.updatedTransactions = updatedTransactions;
    }

    public List<Transaction> getDeletedTransactions() {
        return deletedTransactions;
    }

    public void setDeletedTransactions(List<Transaction> deletedTransactions) {
        this.deletedTransactions = deletedTransactions;
    }
}
