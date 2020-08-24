package com.eightlightyears.sjc.service.data_import;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
@ToString
public class CampaignDailyReportCSVRecord {

    @CsvBindByName(column = "Datasource")
    private String datasource;

    @CsvBindByName(column = "Campaign")
    private String campaign;

    @CsvDate("MM/dd/yy")
    @CsvBindByName(column = "Daily")
    private LocalDate daily;

    @CsvBindByName(column = "Clicks")
    private Long clicks;

    @CsvBindByName(column = "Impressions")
    private Long impressions;

}
