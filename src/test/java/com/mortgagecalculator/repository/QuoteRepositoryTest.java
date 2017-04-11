package com.mortgagecalculator.repository;

import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.model.MortgageProductType;
import com.mortgagecalculator.model.Quote;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
//Test uses a simple in memory database; opted for this for simplicity, but could wire up a test db if one desired
public class QuoteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private QuoteRepository quoteRepository;

    @Test
    public void findQuotesByDateAndLoanAmount() throws Exception {

        LocalDate desiredDate = new LocalDate(2016, 1, 1);

        DailyRate dailyRate1 = new DailyRate("APPLE", MortgageProductType.FIFTEEN_YEAR_FIXED,
                1.250f, 0.111f, desiredDate);
        DailyRate dailyRate2 = new DailyRate("APPLE", MortgageProductType.THIRTY_YEAR_FIXED,
                2.333f, 1.111f, desiredDate);

        DailyRate otherDailyRate = new DailyRate("APPLE", MortgageProductType.FIFTEEN_YEAR_FIXED,
                3.333f, -1.000f, new LocalDate(2016, 1, 2));

        Quote quote1 = new Quote(dailyRate1, 1000000, 50000);
        Quote quote2 = new Quote(dailyRate2, 2000000, 60000);
        Quote quote3 = new Quote(otherDailyRate, 1000000, 75000);

        entityManager.persist(dailyRate1);
        entityManager.persist(dailyRate2);
        entityManager.persist(otherDailyRate);
        entityManager.persist(quote1);
        entityManager.persist(quote2);
        entityManager.persist(quote3);

        List<Quote> retrievedQuotes = quoteRepository.findByApplicableDateAndLoanAmountCents(desiredDate, 2000000);
        assertEquals(1, retrievedQuotes.size());
        assertTrue(retrievedQuotes.contains(quote2));
    }
}
