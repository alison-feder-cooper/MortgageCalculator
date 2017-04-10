package com.mortgagecalculator.service;

import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.model.MortgageProductType;
import com.mortgagecalculator.model.ParValueDailyRate;
import com.mortgagecalculator.repository.DailyRateRepository;
import com.mortgagecalculator.repository.ParValueDailyRateRepository;
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

    @Autowired
    private DailyRateRepository dailyRateRepository;

    @Autowired
    private ParValueDailyRateRepository parValueDailyRateRepository;

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

    @Test
    public void cacheParValues() throws Exception {
        LocalDate desiredDate = new LocalDate(2016, 1, 1);

        //APPLE lender rates for desired date
        DailyRate appleDailyRate1 = new DailyRate("APPLE", MortgageProductType.FIFTEEN_YEAR_FIXED,
                1.250f, 0.105f, desiredDate);
        DailyRate appleDailyRate2 = new DailyRate("APPLE", MortgageProductType.FIFTEEN_YEAR_FIXED,
                0.250f, 1.111f, desiredDate);
        DailyRate appleParValueRate = new DailyRate("APPLE", MortgageProductType.FIFTEEN_YEAR_FIXED,
                2.250f, -0.104f, desiredDate);

        //BANANA lender rates for desired date
        DailyRate bananaDailyRate1 = new DailyRate("BANANA", MortgageProductType.FIFTEEN_YEAR_FIXED,
                4.250f, -4.000f, desiredDate);
        DailyRate bananaParValueRate = new DailyRate("BANANA", MortgageProductType.FIFTEEN_YEAR_FIXED,
                0.005f, 0.100f, desiredDate);

        //BANANA lender rate for different date
        DailyRate bananaParValueRateDayBefore = new DailyRate("BANANA", MortgageProductType.FIFTEEN_YEAR_FIXED,
                0.005f, 0.005f, desiredDate.minusDays(1));

        dailyRateRepository.save(appleDailyRate1);
        dailyRateRepository.save(appleDailyRate2);
        dailyRateRepository.save(bananaDailyRate1);

        dailyRateRepository.save(appleParValueRate);
        dailyRateRepository.save(bananaParValueRate);

        dailyRateRepository.save(bananaParValueRateDayBefore);

        List<DailyRate> cachedParValues = dailyRateService.cacheParValueRates(desiredDate);
        assertEquals(2, cachedParValues.size());
        assertTrue(cachedParValues.contains(appleParValueRate));
        assertTrue(cachedParValues.contains(bananaParValueRate));

        List<ParValueDailyRate> retrievedParValues = parValueDailyRateRepository.findByApplicableDate(desiredDate);
        assertEquals(2, retrievedParValues.size());
        for (ParValueDailyRate parValueDailyRate : retrievedParValues) {
            assertTrue(cachedParValues.contains(parValueDailyRate.getDailyRate()));
        }
    }
}
