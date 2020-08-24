package com.eightlightyears.sjc.service.data_access;

import com.eightlightyears.sjc.service.data_access.dto.CtrDTO;
import com.eightlightyears.sjc.service.data_access.dto.ImpressionsOverTimeDTO;
import com.eightlightyears.sjc.service.data_access.dto.TotalClicksDTO;

import java.time.LocalDate;
import java.util.List;

public interface DataAccessService {

    List<TotalClicksDTO> totalClicks(List<String> groupBy, LocalDate dateFrom, LocalDate dateTo, String campaignFilter, String datasourceFilter);

    List<CtrDTO> ctr(List<String> groupBy, LocalDate dateFrom, LocalDate dateTo, String campaignFilter, String datasourceFilter);

    List<ImpressionsOverTimeDTO> impressionsOverTime(List<String> groupBy, LocalDate dateFrom, LocalDate dateTo, String campaignFilter, String datasourceFilter);
}
