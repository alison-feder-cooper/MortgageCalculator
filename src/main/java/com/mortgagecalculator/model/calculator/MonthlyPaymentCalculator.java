package com.mortgagecalculator.model.calculator;

public interface MonthlyPaymentCalculator {

    int MONTHS_IN_YEAR = 12;

    //if we had other interest rate information, such adjustment, I'd put the second param here as a model
    //individual calculators would consume the parts of the model that they care about. but without that information
    //in the sample input, from the research I did into monthly payment calculations this is the most this interface
    //can know about. Or that information could be provided to a constructor.

    //we'd also then return, perhaps, a map of monthly payments, of year to monthly payment amount, for example
    //perhaps the calculator concept wouldn't adhere to an interface so well in that case, but without more information
    //at this time I opted for an interface
    long calculateMonthlyPaymentAmountCents(long principalLoanAmountCents, float annualInterestRate);
}
