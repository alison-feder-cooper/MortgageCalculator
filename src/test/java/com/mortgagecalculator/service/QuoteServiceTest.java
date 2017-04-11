package com.mortgagecalculator.service;

import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.model.MortgageProductType;
import com.mortgagecalculator.model.ParValueDailyRate;
import com.mortgagecalculator.model.Quote;
import com.mortgagecalculator.repository.DailyRateRepository;
import com.mortgagecalculator.repository.ParValueDailyRateRepository;
import com.mortgagecalculator.repository.QuoteRepository;
import org.assertj.core.util.Sets;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class QuoteServiceTest {

    @Autowired
    private DailyRateRepository dailyRateRepository;

    @Autowired
    private ParValueDailyRateRepository parValueDailyRateRepository;

    @Autowired
    private QuoteRepository quoteRepository;

    @Autowired
    private QuoteService quoteService;

    @Test
    public void createQuotes() {
        LocalDate applicableDate = new LocalDate(2016, 1, 1);

        DailyRate appleDailyRate = new DailyRate("APPLE", MortgageProductType.FIFTEEN_YEAR_FIXED,
                1.250f, 0.105f, applicableDate);
        DailyRate bananaDailyRate = new DailyRate("BANANA", MortgageProductType.THIRTY_YEAR_FIXED,
                1.350f, 0.005f, applicableDate);
        ParValueDailyRate appleParValueDailyRate = ParValueDailyRate.fromDailyRate(appleDailyRate);
        ParValueDailyRate bananaParValueDailyRate = ParValueDailyRate.fromDailyRate(bananaDailyRate);

        dailyRateRepository.save(appleDailyRate);
        dailyRateRepository.save(bananaDailyRate);
        parValueDailyRateRepository.save(appleParValueDailyRate);
        parValueDailyRateRepository.save(bananaParValueDailyRate);

        long loanAmountCents = 10000000;
        Set<Quote> generatedQuotes = quoteService.createQuotes(
                Sets.newLinkedHashSet(appleParValueDailyRate, bananaParValueDailyRate), loanAmountCents);

        Set<Quote> storedQuotes = quoteRepository.findByApplicableDateAndLoanAmountCents(applicableDate, loanAmountCents);

        assertEquals(2, generatedQuotes.size());
        assertThat(generatedQuotes, is(storedQuotes));

        //don't assert any calculation logic results, since that'll be covered in calculator tests
    }
}
