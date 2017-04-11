package com.mortgagecalculator.model.parser;

import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.model.MortgageProductType;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class DailyRateParserTest {

    @Test
    public void validInput30YearFixed() {

        //given
        String fileLine = "APPLE,FNMA30YRFXCF,3.250,-1.165";
        LocalDate applicableDate = new LocalDate(2017, 4, 1);

        //when
        DailyRate parsedDailyRate = new DailyRateParser().fromLine(fileLine, applicableDate);

        //then
        assertEquals("APPLE", parsedDailyRate.getLenderName());
        assertEquals(MortgageProductType.THIRTY_YEAR_FIXED, parsedDailyRate.getMortgageProductType());
        assertEquals(3.250f, parsedDailyRate.getAnnualInterestRate(), 0);
        assertEquals(-1.165f, parsedDailyRate.getPrice(), 0);
        assertEquals(applicableDate, parsedDailyRate.getApplicableDate());
    }

    @Test
    public void validInput15YearFixed() {

        //given
        String fileLine = "APPLE,FNMA15YRFXCF,1.250,1.100";
        LocalDate applicableDate = new LocalDate(2017, 3, 2);

        //when
        DailyRate parsedDailyRate = new DailyRateParser().fromLine(fileLine, applicableDate);

        //then
        assertEquals("APPLE", parsedDailyRate.getLenderName());
        assertEquals(MortgageProductType.FIFTEEN_YEAR_FIXED, parsedDailyRate.getMortgageProductType());
        assertEquals(1.250f, parsedDailyRate.getAnnualInterestRate(), 0);
        assertEquals(1.100f, parsedDailyRate.getPrice(), 0);
        assertEquals(applicableDate, parsedDailyRate.getApplicableDate());
    }

    @Test
    public void validInput5_1_Adjustable() {

        //given
        String fileLine = "BANANA,FNMA51ARMCF,2.222,0.111";
        LocalDate applicableDate = new LocalDate(2017, 2, 10);

        //when
        DailyRate parsedDailyRate = new DailyRateParser().fromLine(fileLine, applicableDate);

        //then
        assertEquals("BANANA", parsedDailyRate.getLenderName());
        assertEquals(MortgageProductType.FIVE_ONE_ADJUSTABLE, parsedDailyRate.getMortgageProductType());
        assertEquals(2.222f, parsedDailyRate.getAnnualInterestRate(), 0);
        assertEquals(0.111f, parsedDailyRate.getPrice(), 0);
        assertEquals(applicableDate, parsedDailyRate.getApplicableDate());
    }

    @Test
    public void validInput7_1_Adjustable() {

        //given
        String fileLine = "BANANA,FNMA71ARMCF,3.000,0.122";
        LocalDate applicableDate = new LocalDate(2017, 1, 11);

        //when
        DailyRate parsedDailyRate = new DailyRateParser().fromLine(fileLine, applicableDate);

        //then
        assertEquals("BANANA", parsedDailyRate.getLenderName());
        assertEquals(MortgageProductType.SEVEN_ONE_ADJUSTABLE, parsedDailyRate.getMortgageProductType());
        assertEquals(3.000f, parsedDailyRate.getAnnualInterestRate(), 0);
        assertEquals(0.122f, parsedDailyRate.getPrice(), 0);
        assertEquals(applicableDate, parsedDailyRate.getApplicableDate());
    }

    @Test
    public void validInput10_1_Adjustable() {
        //given
        String fileLine = "BANANA,FNMA101ARMCF,0.008,-4.111";
        LocalDate applicableDate = new LocalDate(2017, 3, 10);

        //when
        DailyRate parsedDailyRate = new DailyRateParser().fromLine(fileLine, applicableDate);

        //then
        assertEquals("BANANA", parsedDailyRate.getLenderName());
        assertEquals(MortgageProductType.TEN_ONE_ADJUSTABLE, parsedDailyRate.getMortgageProductType());
        assertEquals(0.008f, parsedDailyRate.getAnnualInterestRate(), 0);
        assertEquals(-4.111f, parsedDailyRate.getPrice(), 0);
        assertEquals(applicableDate, parsedDailyRate.getApplicableDate());

    }

    @Test(expected = NullPointerException.class)
    public void invalidInput_nullLine() {
        assertNull(new DailyRateParser().fromLine(null, new LocalDate()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidInput_tooShort() {
        new DailyRateParser().fromLine("BANANA,FNMA101ARMCF,0.008", new LocalDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidInput_tooLong() {
        new DailyRateParser().fromLine("BANANA,FNMA101ARMCF,0.008, 1.111, ABCD", new LocalDate());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidInput_unknownMortgageProductType() {
        new DailyRateParser().fromLine("BANANA,DOUGHNUTMORTGAGE,0.008, 1.111", new LocalDate());
    }

    @Test(expected = NumberFormatException.class)
    public void invalidInput_invalidInterestRate() {
        new DailyRateParser().fromLine("BANANA,FNMA101ARMCF,abcd, 1.111", new LocalDate());
    }

    @Test(expected = NumberFormatException.class)
    public void invalidInput_invalidPrice() {
        new DailyRateParser().fromLine("BANANA,FNMA101ARMCF,1.111, abcd", new LocalDate());
    }
}
