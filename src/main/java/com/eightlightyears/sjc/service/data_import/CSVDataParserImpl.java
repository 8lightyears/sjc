package com.eightlightyears.sjc.service.data_import;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CSVDataParserImpl implements CSVDataParser {

    @Override
    public List<CampaignDailyReportCSVRecord> parse(Reader reader) {
        Class clazz = CampaignDailyReportCSVRecord.class;
        HeaderColumnNameMappingStrategy ms = new HeaderColumnNameMappingStrategy();
        ms.setType(clazz);

        CsvToBean cb = new CsvToBeanBuilder(reader)
                .withType(clazz)
                .withMappingStrategy(ms)
                .withIgnoreQuotations(true)
                .withIgnoreEmptyLine(true)
                .withSeparator(',')
                .build();

        List<CampaignDailyReportCSVRecord> list = cb.parse();

        return list;
    }

    @Override
    public List<CampaignDailyReportCSVRecord> parse(InputStream i) {
        BOMInputStream bomIn = new BOMInputStream(i);

        try (InputStreamReader isr = new InputStreamReader(bomIn, StandardCharsets.UTF_8)) {
            return parse(isr);
        } catch (IOException e) {
            //TODO
            throw new RuntimeException("error", e);
        }

    }
}
