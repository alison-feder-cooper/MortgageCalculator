package com.mortgagecalculator.task;

import com.mortgagecalculator.service.DailyRateService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private DailyRateService dailyRateService;

    //Scheduled to run ingestion every weekday at 5 am
    @Scheduled(cron = "0 0 5 * * MON-FRI")
    public void ingestAndFindParValueRates() {
        LOG.info("Beginning file ingestion at {}", DateTime.now());
        //TODO property based on env
        String directoryName = "src/test/resources/";
        try {
            dailyRateService.ingestRatesForDate(LocalDate.now(), directoryName);
        } catch (Exception e) {
            LOG.error("Error ingesting files for {} ", LocalDate.now(), e);
            return;
        }

        LOG.info("Ending file ingestion at {}", DateTime.now());

        LOG.info("Beginning par value storage at {}", DateTime.now());
        dailyRateService.cacheParValueRates(LocalDate.now());
        LOG.info("Ending par value storage at {}", DateTime.now());


    }
}
