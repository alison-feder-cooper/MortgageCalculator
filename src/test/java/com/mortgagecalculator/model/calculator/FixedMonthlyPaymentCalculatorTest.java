package com.mortgagecalculator.model.calculator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FixedMonthlyPaymentCalculatorTest {

    //TODO, flush out with more test cases
    @Test
    public void fixedMonthlyPayment() {

        MonthlyPaymentCalculator fifteenYearFixedCalculator = FixedMonthlyPaymentCalculator.fifteenYearFixedCalculator();

        int loanAmountCents = 10000000;
        float annualInterestRate = 0.06f;

        long monthlyPaymentAmountCents = fifteenYearFixedCalculator
                .calculateMonthlyPaymentAmountCents(loanAmountCents, annualInterestRate);

        assertEquals(84385, monthlyPaymentAmountCents);
    }
}
