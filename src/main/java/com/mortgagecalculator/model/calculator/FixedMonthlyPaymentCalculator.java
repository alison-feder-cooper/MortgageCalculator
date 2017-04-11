package com.mortgagecalculator.model.calculator;

public class FixedMonthlyPaymentCalculator implements MonthlyPaymentCalculator {

    private final int mortgageTermYears;

    //package private, so that can delegate to this in the adjustable calculator
    FixedMonthlyPaymentCalculator(int mortgageTermYears) {
        this.mortgageTermYears = mortgageTermYears;
    }

    public static MonthlyPaymentCalculator thirtyYearFixedCalculator() {
        return new FixedMonthlyPaymentCalculator(30);
    }

    public static MonthlyPaymentCalculator fifteenYearFixedCalculator() {
        return new FixedMonthlyPaymentCalculator(15);
    }

    @Override
    public long calculateMonthlyPaymentAmountCents(long principalLoanAmountCents, float annualInterestRate) {
        //monthlyPayment = principal [ monthlyInterestRate(1 + monthlyInterestRate)^totalPayments ] / [ (1 + monthlyInterestRate)^(totalPayments) -1]
        int totalPayments = MONTHS_IN_YEAR * mortgageTermYears;
        double monthlyInterestRate = annualInterestRate / MONTHS_IN_YEAR;
        double onePlusMonthlyInterestRate = 1 + monthlyInterestRate;
        double exponentialTerm = Math.pow(onePlusMonthlyInterestRate, totalPayments);
        double numerator = principalLoanAmountCents * monthlyInterestRate * exponentialTerm;
        double denominator = exponentialTerm - 1;
        //floors value of fractional cents
        return (long) (numerator / denominator);
    }
}
