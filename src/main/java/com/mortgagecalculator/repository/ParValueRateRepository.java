package com.mortgagecalculator.repository;

import com.mortgagecalculator.model.DailyRate;
import org.joda.time.LocalDate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//serves as a cache of par value rates for the day, so that we only find them once, instead of per customer request
//TODO
@Repository
public interface ParValueRateRepository extends CrudRepository<DailyRate, Long> {

    List<DailyRate> findByApplicableDate(LocalDate applicableDate);
}
