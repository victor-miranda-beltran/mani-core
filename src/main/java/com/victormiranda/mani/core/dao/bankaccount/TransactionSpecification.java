package com.victormiranda.mani.core.dao.bankaccount;

import com.victormiranda.mani.core.model.TransactionCategory;
import com.victormiranda.mani.core.service.transaction.TransactionFilter;
import org.hibernate.Transaction;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification implements Specification<Transaction> {

    private final TransactionFilter transactionFilter;

    public TransactionSpecification(TransactionFilter transactionFilter) {
        this.transactionFilter = transactionFilter;
    }

    @Override
    public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        final List<Predicate> predicates = new ArrayList<>();

        if (transactionFilter.getStart() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("dateAuthorization"), transactionFilter.getStart()));
        }

        if (transactionFilter.getEnd() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("dateAuthorization"), transactionFilter.getEnd()));
        }

        if (transactionFilter.getFlow() != null) {
            predicates.add(cb.equal(root.get("flow"), transactionFilter.getFlow()));
        }

        if (transactionFilter.getCategory() != null) {
            TransactionCategory tCat = new TransactionCategory();
            tCat.setId(transactionFilter.getCategory().getId());
            predicates.add(cb.equal(root.get("category"), tCat));
        }

        return andTogether(predicates, cb);
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
