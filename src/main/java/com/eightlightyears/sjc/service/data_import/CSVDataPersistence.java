package com.eightlightyears.sjc.service.data_import;

import java.util.List;

public interface CSVDataPersistence {
    void persist(List<CampaignDailyReportCSVRecord> records);
}
