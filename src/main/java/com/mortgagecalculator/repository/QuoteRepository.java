package com.mortgagecalculator.repository;

import com.mortgagecalculator.model.Quote;
import org.joda.time.LocalDate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface QuoteRepository extends CrudRepository<Quote, Long> {

    Set<Quote> findByApplicableDateAndLoanAmountCents(LocalDate applicableDate, long loanAmountCents);
}
