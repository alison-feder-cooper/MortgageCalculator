package com.mortgagecalculator.web;

import com.mortgagecalculator.model.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ApiController {

    //TODO

    //interpreting the spec to have this endpoint return the quotes for today, since the only parameter is loan amount /
    //doesn't include date
    @RequestMapping(method = RequestMethod.GET, value = "/quotes_for_today")
    public List<Quote> optimalQuotesForToday(@RequestParam("loan_amount_cents") long loanAmountCents) {
        return null;
    }
}