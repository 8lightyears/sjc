package com.eightlightyears.sjc.service.data_access.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class ImpressionsOverTimeDTO {
    List<Object> group;
    List<DailyImpressionsOverTimeDTO> dailyImpressionsOverTime;

    @AllArgsConstructor
    @Getter
    @Setter
    public static class DailyImpressionsOverTimeDTO {
        LocalDate date;
        Long value;
    }
}
