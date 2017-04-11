package com.mortgagecalculator.model.response;

import com.mortgagecalculator.model.Quote;

public class QuoteResponse {

    //could store this on MortgageProductType, with a different description per type. For now, just keeping this simple
    //as static strings, one generic explanation for fixed and one for adjustable
    private static final String FIXED_DESCRIPTION = "This monthly payment amount is fixed; it will not change over " +
            "time.";
    private static final String ADJUSTABLE_DESCRIPTION_START = "This monthly payment amount is adjustable. " +
            "The interest rate will adjust after ";
    private static final String ADJUSTABLE_DESCRIPTION_END = " years.";

    private Quote quote;
    private String description;

    public static QuoteResponse fromQuote(Quote quote) {
        QuoteResponse response = new QuoteResponse();
        response.quote = quote;
        response.description = getDescription(quote);
        return response;
    }

    public Quote getQuote() {
        return quote;
    }

    public String getDescription() {
        return description;
    }

    private static String getDescription(Quote quote) {
        return quote.isFixed() ? FIXED_DESCRIPTION :
                ADJUSTABLE_DESCRIPTION_START + quote.getFixedYears() + ADJUSTABLE_DESCRIPTION_END;
    }
}
