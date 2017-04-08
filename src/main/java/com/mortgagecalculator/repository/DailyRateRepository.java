package com.mortgagecalculator.repository;

import com.mortgagecalculator.model.DailyRate;
import org.joda.time.LocalDate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyRateRepository extends CrudRepository<DailyRate, Long> {

    List<DailyRate> findByApplicableDate(LocalDate applicableDate);
}
