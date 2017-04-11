package com.mortgagecalculator.model.response;

import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.model.MortgageProductType;
import com.mortgagecalculator.model.Quote;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QuoteResponseTest {

    private LocalDate applicableDate = new LocalDate(2016, 1, 1);

    @Test
    public void fixedQuoteResponse() {
        Quote quote = createQuoteForType(MortgageProductType.FIFTEEN_YEAR_FIXED);
        QuoteResponse quoteResponse = QuoteResponse.fromQuote(quote);
        assertEquals(quote, quoteResponse.getQuote());
        assertEquals("This monthly payment amount is fixed; it will not change over time.",
                quoteResponse.getDescription());
    }

    @Test
    public void adjustable5YearQuoteResponse() {
        Quote quote = createQuoteForType(MortgageProductType.FIVE_ONE_ADJUSTABLE);
        QuoteResponse quoteResponse = QuoteResponse.fromQuote(quote);
        assertEquals(quote, quoteResponse.getQuote());
        assertEquals("This monthly payment amount is adjustable. The interest rate will adjust after 5 years.",
                quoteResponse.getDescription());
    }

    @Test
    public void adjustable7YearQuoteResponse() {
        Quote quote = createQuoteForType(MortgageProductType.SEVEN_ONE_ADJUSTABLE);
        QuoteResponse quoteResponse = QuoteResponse.fromQuote(quote);
        assertEquals(quote, quoteResponse.getQuote());
        assertEquals("This monthly payment amount is adjustable. The interest rate will adjust after 7 years.",
                quoteResponse.getDescription());
    }

    @Test
    public void adjustable10YearQuoteResponse() {
        Quote quote = createQuoteForType(MortgageProductType.TEN_ONE_ADJUSTABLE);
        QuoteResponse quoteResponse = QuoteResponse.fromQuote(quote);
        assertEquals(quote, quoteResponse.getQuote());
        assertEquals("This monthly payment amount is adjustable. The interest rate will adjust after 10 years.",
                quoteResponse.getDescription());
    }

    private Quote createQuoteForType(MortgageProductType mortgageProductType) {
        DailyRate dailyRate = new DailyRate("APPLE", mortgageProductType,
                1.250f, 0.111f, applicableDate);
        return new Quote(dailyRate, 1000000, 200000);
    }
}
