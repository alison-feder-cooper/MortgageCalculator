package com.mortgagecalculator.repository;

import com.mortgagecalculator.model.ParValueDailyRate;
import org.joda.time.LocalDate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//serves as a cache of par value rates for the day, so that we only find them once, instead of per customer request
@Repository
public interface ParValueDailyRateRepository extends CrudRepository<ParValueDailyRate, Long> {

    List<ParValueDailyRate> findByApplicableDate(LocalDate applicableDate);
}
