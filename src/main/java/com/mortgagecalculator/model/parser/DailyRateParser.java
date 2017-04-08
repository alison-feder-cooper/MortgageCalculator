package com.mortgagecalculator.model.parser;

import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.model.MortgageProductType;
import org.joda.time.LocalDate;

/*
would ideally make a generic parser, pass it a schema, and then have different parsers inherit from it, typed by output.
For simplicity, writing a very specific parser for the spec use case, which takes in a line, assumes it is comma separated,
and tries to turn it into a DailyRate.

I'm not an expert in Java CSV parsing; I imagine there are good libraries for this, but nothing I saw from cursory research
fit the use case for taking a string for the product code and mapping it to an enum with a different name.
 */
public class DailyRateParser {

    private static final int LENDER_NAME_INDEX = 0;
    private static final int PRODUCT_INDEX = 1;
    private static final int INTEREST_RATE_INDEX = 2;
    private static final int PRICE_INDEX = 3;
    private static final int NUMBER_OF_DATA_FIELDS = 4;

    public DailyRate fromLine(String line, LocalDate applicableDate) {
        if (line == null || applicableDate == null) {
            throw new NullPointerException("Must provide line and applicable date");
        }

        String[] tokens = line.split(",");
        //4 tokens, for lender, interest rate, and price
        if (tokens.length != NUMBER_OF_DATA_FIELDS) {
            throw new IllegalArgumentException("Invalid line input; not enough fields: [line = " + line + "]");
        }

        MortgageProductType mortgageProductType = MortgageProductType.getByCode(tokens[PRODUCT_INDEX]);
        if (mortgageProductType == null) {
            throw new IllegalArgumentException("Unknown mortgage product type: " + tokens[PRODUCT_INDEX]);
        }

        float interestRate = Float.parseFloat(tokens[INTEREST_RATE_INDEX]);
        float price = Float.parseFloat(tokens[PRICE_INDEX]);

        return new DailyRate(tokens[LENDER_NAME_INDEX], mortgageProductType, interestRate, price, applicableDate);
    }
}
