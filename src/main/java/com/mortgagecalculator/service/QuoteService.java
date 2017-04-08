package com.mortgagecalculator.service;

import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.model.Quote;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

//TODO
@Component
@Transactional
public class QuoteService {

    //TODO
    public List<Quote> createQuotes(List<DailyRate> dailyRates) {
        return new ArrayList<>();
    }
}
