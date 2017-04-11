package com.mortgagecalculator.repository;

import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.model.MortgageProductType;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
//Test uses a simple in memory database; opted for this for simplicity, but could wire up a test db if one desired
public class DailyRateRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DailyRateRepository dailyRateRepository;

    @Test
    public void findDailyRatesByDate() throws Exception {

        LocalDate desiredDate = new LocalDate(2016, 1, 1);

        DailyRate dailyRate1 = new DailyRate("APPLE", MortgageProductType.FIFTEEN_YEAR_FIXED,
                1.250f, 0.111f, desiredDate);
        DailyRate dailyRate2 = new DailyRate("APPLE", MortgageProductType.THIRTY_YEAR_FIXED,
                2.333f, 1.111f, desiredDate);

        DailyRate otherDailyRate = new DailyRate("APPLE", MortgageProductType.FIFTEEN_YEAR_FIXED,
                3.333f, -1.000f, new LocalDate(2016, 1, 2));

        entityManager.persist(dailyRate1);
        entityManager.persist(dailyRate2);
        entityManager.persist(otherDailyRate);

        Set<DailyRate> retrievedDailyRates = dailyRateRepository.findByApplicableDate(desiredDate);
        assertThat(retrievedDailyRates, containsInAnyOrder(dailyRate1, dailyRate2));
    }
}
