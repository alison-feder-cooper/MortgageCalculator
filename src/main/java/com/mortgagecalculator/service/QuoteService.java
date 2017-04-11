package com.mortgagecalculator.service;

import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.model.MortgageProductType;
import com.mortgagecalculator.model.ParValueDailyRate;
import com.mortgagecalculator.model.Quote;
import com.mortgagecalculator.model.calculator.FixedMonthlyPaymentCalculator;
import com.mortgagecalculator.model.calculator.MonthlyPaymentCalculator;
import com.mortgagecalculator.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Transactional
public class QuoteService {

    //TODO maybe wire this in as a constructor param via spring dependency injection? that way if there are new
    //calculators, we just change some configs instead of this service code?
    private static final Map<MortgageProductType, MonthlyPaymentCalculator> CALCULATORS_MAP;

    static {
        CALCULATORS_MAP = new HashMap<>();
        CALCULATORS_MAP.put(MortgageProductType.FIFTEEN_YEAR_FIXED, FixedMonthlyPaymentCalculator.fifteenYearFixedCalculator());
        CALCULATORS_MAP.put(MortgageProductType.THIRTY_YEAR_FIXED, FixedMonthlyPaymentCalculator.thirtyYearFixedCalculator());
        //TODO 5-1, 7-1, 10-1 calculators after figure out exactly how to calculate the monthly rate
    }

    @Autowired
    private QuoteRepository quoteRepository;

    public List<Quote> createQuotes(List<ParValueDailyRate> parValueDailyRates, long loanAmountCents) {
        List<Quote> quotes = new ArrayList<>();
        for (ParValueDailyRate parValueDailyRate : parValueDailyRates) {
            DailyRate dailyRate = parValueDailyRate.getDailyRate();
            MonthlyPaymentCalculator calculator = CALCULATORS_MAP.get(dailyRate.getMortgageProductType());
            long monthlyPaymentAmountCents = calculator.calculateMonthlyPaymentAmountCents(loanAmountCents,
                    dailyRate.getAnnualInterestRate());
            quotes.add(new Quote(dailyRate, loanAmountCents, monthlyPaymentAmountCents));
        }
        quoteRepository.save(quotes);
        return Collections.unmodifiableList(quotes);
    }
}
