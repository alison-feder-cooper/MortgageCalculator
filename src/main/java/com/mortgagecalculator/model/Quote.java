package com.mortgagecalculator.model;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//TODO
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long id;

    //TODO foreign key daily rate

    @Column(nullable = false)
    private long loanAmountCents;

    @Column(nullable = false)
    private long monthlyPaymentAmountCents;

    //applicable date is also a first-order concept for a quote; so storing it here as well, instead of having to
    //join on daily rate
    @Column(nullable = false)
    private LocalDate applicableDate;

    @Column(nullable = false)
    private DateTime createdDateTime;
}
