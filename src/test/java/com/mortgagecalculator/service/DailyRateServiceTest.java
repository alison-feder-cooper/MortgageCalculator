package com.mortgagecalculator.service;

import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.model.MortgageProductType;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class DailyRateServiceTest {

    @Autowired
    private DailyRateService dailyRateService;

    @Test
    public void validInput_noPreexistingRecordsForDate() throws IOException {

        LocalDate applicableDay = new LocalDate(2017, 4, 1);
        List<DailyRate> dailyRates = dailyRateService.ingestRatesForDate(applicableDay,
                "src/test/resources/service/");

        assertEquals(3, dailyRates.size());
        assertTrue(dailyRates.contains(new DailyRate("APPLE", MortgageProductType.FIFTEEN_YEAR_FIXED,
                2.875f, 0.979f, applicableDay)));
        assertTrue(dailyRates.contains(new DailyRate("APPLE", MortgageProductType.FIFTEEN_YEAR_FIXED,
                3.000f, 0.447f, applicableDay)));
        assertTrue(dailyRates.contains(new DailyRate("BANANA", MortgageProductType.THIRTY_YEAR_FIXED,
                3.750f, 0.706f, applicableDay)));
    }

    @Test(expected = IllegalStateException.class)
    public void invalidInput_preexistingRecordsForDate() throws IOException {

        LocalDate applicableDay = new LocalDate(2017, 4, 1);
        dailyRateService.ingestRatesForDate(applicableDay, "src/test/resources/service/");
        //attempt to read again
        dailyRateService.ingestRatesForDate(applicableDay, "src/test/resources/service/");
    }
}
