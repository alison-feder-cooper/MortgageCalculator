package com.mortgagecalculator.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.model.MortgageProductType;
import com.mortgagecalculator.model.ParValueDailyRate;
import com.mortgagecalculator.model.Quote;
import com.mortgagecalculator.repository.ParValueDailyRateRepository;
import com.mortgagecalculator.repository.QuoteRepository;
import com.mortgagecalculator.service.QuoteService;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ApiControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private QuoteRepository quoteRepository;
    @MockBean
    private ParValueDailyRateRepository parValueDailyRateRepository;
    @MockBean
    private QuoteService quoteService;

    @Test
    public void getQuotesForTodaySucceedsWithPreexistingQuotes() throws Exception {
        given(quoteRepository.findByApplicableDateAndLoanAmountCents(any(LocalDate.class), eq(10000000L)))
                .willReturn(getQuotes());

        mvc.perform(get("/api/quotes_for_today")
                .contentType(MediaType.APPLICATION_JSON)
                .param("loan_amount_cents", "10000000"))
                .andExpect(status().is2xxSuccessful())
                //TODO more robust matchers for evaluating QuoteResponse directly
                .andExpect(content().string(containsString("\"lenderName\":\"APPLE\"")))
                .andExpect(content().string(containsString("\"mortgageProductType\":\"FIFTEEN_YEAR_FIXED\"")))
                .andExpect(content().string(containsString("\"annualInterestRate\":0.06")))
                .andExpect(content().string(containsString("\"description\":\"This monthly payment amount is fixed; it will not change over time.\"")))
                .andExpect(content().string(containsString("\"lenderName\":\"BANANA\"")))
                .andExpect(content().string(containsString("\"mortgageProductType\":\"FIVE_ONE_ADJUSTABLE\"")))
                .andExpect(content().string(containsString("\"annualInterestRate\":0.058")))
                .andExpect(content().string(containsString("\"description\":\"This monthly payment amount is adjustable. The interest rate will adjust after 5 years.\"")));
    }

    @Test
    public void getQuotesForTodaySucceedsCreatingNewQuotes() throws Exception {
        given(quoteRepository.findByApplicableDateAndLoanAmountCents(any(LocalDate.class), eq(10000000L)))
                .willReturn(new HashSet<>());
        given(parValueDailyRateRepository.findByApplicableDate(any(LocalDate.class)))
                .willReturn(getParValues());
        given(quoteService.createQuotes(eq(getParValues()), eq(10000000L)))
                .willReturn(getQuotes());

        mvc.perform(get("/api/quotes_for_today")
                .contentType(MediaType.APPLICATION_JSON)
                .param("loan_amount_cents", "10000000"))
                .andExpect(status().is2xxSuccessful())
                //TODO more robust matchers for evaluating QuoteResponse directly
                .andExpect(content().string(containsString("\"lenderName\":\"APPLE\"")))
                .andExpect(content().string(containsString("\"mortgageProductType\":\"FIFTEEN_YEAR_FIXED\"")))
                .andExpect(content().string(containsString("\"annualInterestRate\":0.06")))
                .andExpect(content().string(containsString("\"description\":\"This monthly payment amount is fixed; it will not change over time.\"")))
                .andExpect(content().string(containsString("\"lenderName\":\"BANANA\"")))
                .andExpect(content().string(containsString("\"mortgageProductType\":\"FIVE_ONE_ADJUSTABLE\"")))
                .andExpect(content().string(containsString("\"annualInterestRate\":0.058")))
                .andExpect(content().string(containsString("\"description\":\"This monthly payment amount is adjustable. The interest rate will adjust after 5 years.\"")));
    }

    @Test
    public void getQuotesForTodayErrors() throws Exception {
        given(quoteRepository.findByApplicableDateAndLoanAmountCents(any(LocalDate.class), eq(10000000L)))
                .willReturn(new HashSet<>());
        given(parValueDailyRateRepository.findByApplicableDate(any(LocalDate.class)))
                .willReturn(new HashSet<>());

        mvc.perform(get("/api/quotes_for_today")
                .contentType(MediaType.APPLICATION_JSON)
                .param("loan_amount_cents", "10000000"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(status().reason("Par values not available for today"));
    }

    private Set<Quote> getQuotes() {
        Set<Quote> preexistingQuotes = new HashSet<>();
        preexistingQuotes.add(new Quote(
                new DailyRate("APPLE", MortgageProductType.FIFTEEN_YEAR_FIXED,
                        0.06f, 0.100f, LocalDate.now()),
                10000000,
                200000));
        preexistingQuotes.add(new Quote(
                new DailyRate("BANANA", MortgageProductType.FIVE_ONE_ADJUSTABLE,
                        0.058f, 0.095f, LocalDate.now()),
                10000000,
                175000));
        return preexistingQuotes;
    }

    private Set<ParValueDailyRate> getParValues() {
        return null;
    }
}