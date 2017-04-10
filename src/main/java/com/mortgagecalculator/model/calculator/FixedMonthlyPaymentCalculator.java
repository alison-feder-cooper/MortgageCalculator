package com.mortgagecalculator.model.calculator;

public class FixedMonthlyPaymentCalculator implements MonthlyPaymentCalculator {

    private final int mortgageTermYears;

    private FixedMonthlyPaymentCalculator(int mortgageTermYears) {
        this.mortgageTermYears = mortgageTermYears;
    }

    public static FixedMonthlyPaymentCalculator thirtyYearFixedCalculator() {
        return new FixedMonthlyPaymentCalculator(30);
    }

    public static FixedMonthlyPaymentCalculator fifteenYearFixedCalculator() {
        return new FixedMonthlyPaymentCalculator(15);
    }

    @Override
    public long calculateMonthlyPaymentAmountCents(long principalLoanAmountCents, float annualInterestRate) {
        //monthlyPayment = principal [ monthlyInterestRate(1 + monthlyInterestRate)^totalPayments ] / [ (1 + monthlyInterestRate)^(totalPayments) -1]
        int totalPayments = MONTHS_IN_YEAR * mortgageTermYears;
        float monthlyInterestRate = annualInterestRate / MONTHS_IN_YEAR;
        double onePlusMonthlyInterestRate = 1 + monthlyInterestRate;
        double exponentialTerm = Math.pow(onePlusMonthlyInterestRate, totalPayments);
        double numerator = principalLoanAmountCents * monthlyInterestRate * exponentialTerm;
        double denominator = exponentialTerm - 1;
        //floors value of fractional cents
        return (long) (numerator / denominator);
    }
}
