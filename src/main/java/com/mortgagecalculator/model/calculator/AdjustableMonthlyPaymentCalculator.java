package com.mortgagecalculator.model.calculator;

public class AdjustableMonthlyPaymentCalculator implements MonthlyPaymentCalculator {

    private final MonthlyPaymentCalculator fixedPortionCalculator;
    private final int mortgageTermYears;

    //TODO other fields, once provided with adjustabe %
    private AdjustableMonthlyPaymentCalculator(int mortgageTermYears) {
        this.fixedPortionCalculator = new FixedMonthlyPaymentCalculator(mortgageTermYears);
        this.mortgageTermYears = mortgageTermYears;
    }

    //TODO could take adjustable %, too, if we had it, as a parameter to be used by the calculator
    public static MonthlyPaymentCalculator fiveOneAdjustableCalculator() {
        return new AdjustableMonthlyPaymentCalculator(5);
    }

    public static MonthlyPaymentCalculator sevenOneAdjustableCalculator() {
        return new AdjustableMonthlyPaymentCalculator(7);
    }

    public static MonthlyPaymentCalculator tenOneAdjustableCalculator() {
        return new AdjustableMonthlyPaymentCalculator(10);
    }

    //See note in MonthlyPaymentCalculator interface about other information we'd want for adjustable rate mortgages,
    //given having the adjustment rate (or estimated adjustment rate) in the files from lenders.
    @Override
    public long calculateMonthlyPaymentAmountCents(long principalLoanAmountCents, float annualInterestRate) {
        //for now, with adjustable rate information, we can calculate the fixed portion, so will delegate only to the
        // fixed monthly payment calculator
        return this.fixedPortionCalculator.calculateMonthlyPaymentAmountCents(principalLoanAmountCents, annualInterestRate);
    }
}
