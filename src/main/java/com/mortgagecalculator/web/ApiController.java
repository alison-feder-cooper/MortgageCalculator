package com.mortgagecalculator.web;

import com.mortgagecalculator.model.ParValueDailyRate;
import com.mortgagecalculator.model.Quote;
import com.mortgagecalculator.model.response.QuoteResponse;
import com.mortgagecalculator.repository.ParValueDailyRateRepository;
import com.mortgagecalculator.repository.QuoteRepository;
import com.mortgagecalculator.service.QuoteService;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private QuoteService quoteService;

    @Autowired
    private ParValueDailyRateRepository parValueDailyRateRepository;

    //interpreting the spec to have this endpoint return the quotes for today, since the only parameter is loan amount /
    //doesn't include date
    //return as list, even thoug have worked with sets to enforce uniqueness, since response will be JSON
    @RequestMapping(method = RequestMethod.GET, value = "/quotes_for_today")
    public List<QuoteResponse> optimalQuotesForToday(@RequestParam("loan_amount_cents") long loanAmountCents) {
        Set<Quote> quotesForTodayAndForAmount = quoteRepository.findByApplicableDateAndLoanAmountCents(LocalDate.now(), loanAmountCents);
        if (!quotesForTodayAndForAmount.isEmpty()) {
            return createQuoteResponses(quotesForTodayAndForAmount);
        }
        Set<ParValueDailyRate> parValueRatesForToday = parValueDailyRateRepository.findByApplicableDate(LocalDate.now());
        Set<Quote> createdQuotes = quoteService.createQuotes(parValueRatesForToday, loanAmountCents);
        return createQuoteResponses(createdQuotes);
    }

    private List<QuoteResponse> createQuoteResponses(Set<Quote> quotes) {
        List<QuoteResponse> quoteResponses = new ArrayList<>();
        for (Quote quote : quotes) {
            quoteResponses.add(QuoteResponse.fromQuote(quote));
        }
        return quoteResponses;
    }
}