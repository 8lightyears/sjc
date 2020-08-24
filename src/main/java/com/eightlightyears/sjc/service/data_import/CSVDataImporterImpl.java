package com.eightlightyears.sjc.service.data_import;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class CSVDataImporterImpl implements CSVDataImporter {

    @Autowired
    CSVDataParser csvDataParser;

    @Autowired
    CSVDataPersistence csvDataPersistence;

    @Override
    public void importData(InputStream is) {
        List<CampaignDailyReportCSVRecord> csvRecords = csvDataParser.parse(is);
        csvDataPersistence.persist(csvRecords);
    }
}
