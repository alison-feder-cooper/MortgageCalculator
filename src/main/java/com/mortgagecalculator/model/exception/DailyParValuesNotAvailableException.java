package com.mortgagecalculator.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE, reason = "Par values not available for today")
public class DailyParValuesNotAvailableException extends RuntimeException {

    private static final long serialVersionUID = -6791497609517443019L;

    public DailyParValuesNotAvailableException() {
        super("Par values not available for today");
    }
}