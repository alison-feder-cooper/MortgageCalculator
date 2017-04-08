package com.mortgagecalculator.task;

import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.repository.DailyRateRepository;
import com.mortgagecalculator.service.DailyRateIngestionService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasks {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private DailyRateIngestionService dailyRateIngestionService;

    @Autowired
    private DailyRateRepository dailyRateRepository;

    //Scheduled to run ingestion every weekday at 5 am
    @Scheduled(cron = "0 0 5 * * MON-FRI")
    public void ingest() {
        LOG.info("Beginning file ingestion at {}", DateTime.now());
        //TODO property based on env
        String directoryName = "src/test/resources/";
        try {
            dailyRateIngestionService.ingestRatesForDate(LocalDate.now(), directoryName);
        } catch (Exception e) {
            LOG.error("Error ingesting files for {} ", LocalDate.now(), e);
        }

        LOG.info("Ending file ingestion at {}", DateTime.now());
    }

    //TODO make this task dependent on successful completion of ingestion; for simplicity here, just a separate cron
    //task at 6 am
    @Scheduled(cron = "0 0 6 * * MON-FRI")
    public void calculateQuotesForParValueRates() {
        LOG.info("Beginning calculation of quotes for par values at {}", DateTime.now());
        List<DailyRate> parValueDailyRatesForLenders = dailyRateRepository.findParValueDailyRatesForLenders(LocalDate.now());
        //TODO calculate quotes
        LOG.info("Ending calculation of quotes for par values at {}", DateTime.now());
    }

}