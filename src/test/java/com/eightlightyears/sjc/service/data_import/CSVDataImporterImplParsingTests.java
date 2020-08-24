package com.eightlightyears.sjc.service.data_import;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class CSVDataImporterImplParsingTests {

    @InjectMocks
    CSVDataParserImpl csvDataParser;

    @Test
    public void parseTest() {
        String csvFileContent =
                "Datasource,Campaign,Daily,Clicks,Impressions\n" +
                "Google Ads,Adventmarkt Touristik,11/12/19,7,22425\n" +
                "Facebook Ads,Pickerl-Erinnerung,04/24/19,3,163";

        StringReader reader = new StringReader(csvFileContent);

        List<CampaignDailyReportCSVRecord> pdmValues = csvDataParser.parse(reader);
        assertThat(pdmValues).containsExactly(
                CampaignDailyReportCSVRecord.builder().datasource("Google Ads")
                        .campaign("Adventmarkt Touristik")
                        .daily(LocalDate.of(2019, 11, 12))
                        .clicks(7L)
                        .impressions(22425L)
                        .build(),
                CampaignDailyReportCSVRecord.builder().datasource("Facebook Ads")
                        .campaign("Pickerl-Erinnerung")
                        .daily(LocalDate.of(2019, 4, 24))
                        .clicks(3L)
                        .impressions(163L)
                        .build()
        );

    }
}
