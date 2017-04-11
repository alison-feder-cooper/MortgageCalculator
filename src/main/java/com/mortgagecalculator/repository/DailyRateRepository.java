package com.mortgagecalculator.repository;

import com.mortgagecalculator.model.DailyRate;
import org.joda.time.LocalDate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DailyRateRepository extends CrudRepository<DailyRate, Long> {

    Set<DailyRate> findByApplicableDate(LocalDate applicableDate);

    /*I'm new to the JPQL query language; I could not find a way to implement the below query for par value by lender on
    specific date. Subqueries seem limited, and attempting to implement this in raw SQL / as a native query also didn't work.
    I've time-boxed researching this, for the sake of the assignment. Ideally, there'd be a data access method that pulls
    daily rates with min absolute value of price, grouped by lender, for the applicable date.
     */
    /*
"SELECT dr.* FROM " +
            "(SELECT lender, MIN(ABS(price)) AS parValue FROM DailyRates WHERE applicableDate = :applicableDate GROUP BY lender, mortgageProductType) AS lenderParValues " +
            "INNER JOIN DailyRates AS dr " +
            "ON lenderParValues.lender = dr.lender AND lenderParValues.parValue = dr.price " +
            "WHERE dr.applicableDate = :applicableDate"
 */
}

