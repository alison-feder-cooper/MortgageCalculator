package com.mortgagecalculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MortgageCalculatorApplication {

    public static void main(String[] args) {

        SpringApplication.run(MortgageCalculatorApplication.class, args);
    }
}
