package com.eightlightyears.sjc.service.data_import;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;

public interface CSVDataParser {
    List<CampaignDailyReportCSVRecord> parse(Reader reader);
    List<CampaignDailyReportCSVRecord> parse(InputStream i);
}
