package com.victormiranda.mani.core.model;


import com.victormiranda.mani.type.TransactionFlow;

import javax.persistence.*;

@Entity
@Table(name = "category")
public class TransactionCategory implements ManiModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "flow")
    private TransactionFlow flow;


    @ManyToOne
    @JoinColumn(name = "parent_id")
    private TransactionCategory parent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TransactionFlow getFlow() {
        return flow;
    }

    public void setFlow(TransactionFlow flow) {
        this.flow = flow;
    }

    public TransactionCategory getParent() {
        return parent;
    }

    public void setParent(TransactionCategory parent) {
        this.parent = parent;
    }
}
