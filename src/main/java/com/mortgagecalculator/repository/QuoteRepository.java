package com.mortgagecalculator.repository;

import com.mortgagecalculator.model.Quote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

//TODO
@Repository
public interface QuoteRepository extends CrudRepository<Quote, Long> {
}
