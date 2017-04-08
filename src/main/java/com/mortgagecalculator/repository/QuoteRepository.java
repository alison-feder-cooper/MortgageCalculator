package com.mortgagecalculator.repository;

import com.mortgagecalculator.model.Quote;
import org.joda.time.LocalDate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//TODO
@Repository
public interface QuoteRepository extends CrudRepository<Quote, Long> {

    List<Quote> findByApplicableDateAndLoanAmountCents(LocalDate applicableDate, long loanAmountCents);
}
