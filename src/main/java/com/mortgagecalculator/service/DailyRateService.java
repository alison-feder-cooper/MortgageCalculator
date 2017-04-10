package com.mortgagecalculator.service;

import com.mortgagecalculator.model.DailyRate;
import com.mortgagecalculator.model.ParValueDailyRate;
import com.mortgagecalculator.model.parser.DailyRateParser;
import com.mortgagecalculator.repository.DailyRateRepository;
import com.mortgagecalculator.repository.ParValueDailyRateRepository;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

@Component
@Transactional
public class DailyRateService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyRateService.class);
    private static final String LOCAL_DATE_FILE_PATTERN = "MMddyyyy";

    @Autowired
    private DailyRateRepository dailyRateRepository;

    @Autowired
    private ParValueDailyRateRepository parValueDailyRateRepository;

    //TODO consider how to make this idempotent; for now, if there are records for specified day, throw an error
    //consider pulling path from a property; for now, will require it as a param for testing purposes
    public List<DailyRate> ingestRatesForDate(LocalDate date, String path) throws IOException {
        if (hasRecordsForApplicableDate(date)) {
            throw new IllegalStateException("Already ingested records for " + date.toString());
        }

        List<File> files = getFilesToParse(date, path);
        DailyRateParser parser = new DailyRateParser();
        List<DailyRate> dailyRates = new ArrayList<>();

        if (files.isEmpty()) {
            throw new FileNotFoundException("No files for " + date);
        }

        //TODO could open a parallel stream / process files in parallel; for simplicity for this assignment, using
        //a plain old for loop
        String currentLine;
        for (File file : files) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getPath()));
            LOG.info("Reading file {}", file.getPath());

            while ((currentLine = bufferedReader.readLine()) != null) {
                dailyRates.add(parser.fromLine(currentLine, date));
            }
        }

        dailyRateRepository.save(dailyRates);
        return Collections.unmodifiableList(dailyRates);
    }

    public List<DailyRate> cacheParValueRates(LocalDate date) {
        //please see notes in DailyRateRepository; this is a hacky solution. I'm new to JPQL and could not get
        //a native sql query working with spring boot
        List<DailyRate> parValueRatesForLenders = getParValueRatesForLenders(date);
        parValueDailyRateRepository.save(asParValueRates(parValueRatesForLenders));
        return Collections.unmodifiableList(parValueRatesForLenders);

    }

    private boolean hasRecordsForApplicableDate(LocalDate applicableDate) {
        List<DailyRate> ratesForDay = dailyRateRepository.findByApplicableDate(applicableDate);
        return ratesForDay != null && !ratesForDay.isEmpty();
    }

    private List<File> getFilesToParse(LocalDate date, String path) {

        //Not implemented: When download files, e.g. via sftp client, store them elsewhere, e.g. in a bucket in S3.
        //Have root be configurable based on environment, so that in testing it can be a local directory, but in prod
        //it could pull the file from S3.
        File directory = new File(path);
        String filePathPattern = getFilePathPattern(date);
        File[] files = directory.listFiles((File dir, String name) -> name.endsWith(filePathPattern + ".csv"));
        LOG.info("FOUND FILES: {}", Arrays.toString(files));

        return (files == null || files.length == 0) ? new ArrayList<>() : Arrays.asList(files);
    }

    private String getFilePathPattern(LocalDate date) {
        return date.toString(LOCAL_DATE_FILE_PATTERN);
    }

    private List<DailyRate> getParValueRatesForLenders(LocalDate date) {
        List<DailyRate> allRatesForDate = dailyRateRepository.findByApplicableDate(date);
        Map<String, DailyRate> lenderParValueRateMap = new HashMap<>();
        for (DailyRate rate : allRatesForDate) {
            if (!lenderParValueRateMap.containsKey(rate.getLenderName())) {
                lenderParValueRateMap.put(rate.getLenderName(), rate);
                continue;
            }
            String lender = rate.getLenderName();
            DailyRate currentRateForLender = lenderParValueRateMap.get(lender);
            if (Math.abs(rate.getPrice()) < Math.abs(currentRateForLender.getPrice())) {
                lenderParValueRateMap.put(lender, rate);
            }
        }
        return new ArrayList<>(lenderParValueRateMap.values());
    }

    private List<ParValueDailyRate> asParValueRates(List<DailyRate> dailyRates) {
        List<ParValueDailyRate> parValueDailyRates = new ArrayList<>();
        for (DailyRate dailyRate : dailyRates) {
            parValueDailyRates.add(ParValueDailyRate.fromDailyRate(dailyRate));
        }
        return parValueDailyRates;
    }
}
