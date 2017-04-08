package com.mortgagecalculator.web;

import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.model.Quote;
import com.mortgagecalculator.repository.ParValueRateRepository;
import com.mortgagecalculator.repository.QuoteRepository;
import com.mortgagecalculator.service.QuoteService;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private ParValueRateRepository parValueRateRepository;

    //interpreting the spec to have this endpoint return the quotes for today, since the only parameter is loan amount /
    //doesn't include date
    @RequestMapping(method = RequestMethod.GET, value = "/quotes_for_today")
    public List<Quote> optimalQuotesForToday(@RequestParam("loan_amount_cents") long loanAmountCents) {
        List<Quote> quotesForTodayAndForAmount = quoteRepository.findByApplicableDateAndLoanAmountCents(LocalDate.now(), loanAmountCents);
        if (!quotesForTodayAndForAmount.isEmpty()) {
            return quotesForTodayAndForAmount;
        }
        List<DailyRate> parValueRatesForToday = parValueRateRepository.findByApplicableDate(LocalDate.now());
        return quoteService.createQuotes(parValueRatesForToday, loanAmountCents);
    }
}