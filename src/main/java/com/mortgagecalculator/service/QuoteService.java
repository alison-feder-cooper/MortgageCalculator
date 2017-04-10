package com.mortgagecalculator.service;

import com.mortgagecalculator.model.ParValueDailyRate;
import com.mortgagecalculator.model.Quote;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class QuoteService {

    //TODO create quotes from daily rates as they are called for; think about how to model monthly payment calculators
    public List<Quote> createQuotes(List<ParValueDailyRate> parValueDailyRates, long loanAmountCents) {
        return new ArrayList<>();
    }
}
