package com.eightlightyears.sjc.service.data_access;

import com.eightlightyears.sjc.model.entities.DailyReport;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class DataGroup implements Comparable<DataGroup> {
    DataGroupDesc group;
    List<DailyReport> dailyReportList;

    @Override
    public int compareTo(DataGroup o) {
        return group.compareTo(o.getGroup());
    }
}
