package com.mortgagecalculator.model.calculator;

public interface MonthlyPaymentCalculator {

    int MONTHS_IN_YEAR = 12;

    long calculateMonthlyPaymentAmountCents(long principalLoanAmountCents, float annualInterestRate);
}
