package com.eightlightyears.sjc.controller;

import com.eightlightyears.sjc.service.data_access.DataAccessService;
import com.eightlightyears.sjc.service.data_access.dto.CtrDTO;
import com.eightlightyears.sjc.service.data_access.dto.ImpressionsOverTimeDTO;
import com.eightlightyears.sjc.service.data_access.dto.TotalClicksDTO;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/metrics")
@Api
@Slf4j
public class MetricsController {

    @Autowired
    DataAccessService dataAccessService;

    @RequestMapping(value = "/total_clicks", method = RequestMethod.GET)
    public ResponseEntity<List<TotalClicksDTO>> getTotalClicks(@RequestParam(value = "group_by", required = false) List<String> groupBy,
                                            @RequestParam(value = "date_from", required = false) @DateTimeFormat(pattern="MM-dd-yyyy") LocalDate dateFrom,
                                            @RequestParam(value = "date_to", required = false) @DateTimeFormat(pattern="MM-dd-yyyy")  LocalDate dateTo,
                                            @RequestParam(value = "campaign", required = false) String campaignFilter,
                                            @RequestParam(value = "datasource", required = false) String datasourceFilter) {
        List<TotalClicksDTO> result = dataAccessService.totalClicks(groupBy, dateFrom, dateTo, campaignFilter, datasourceFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/ctr", method = RequestMethod.GET)
    public ResponseEntity<List<CtrDTO>> getCTR(@RequestParam(value = "group_by", required = false) List<String> groupBy,
                                                          @RequestParam(value = "date_from", required = false) @DateTimeFormat(pattern="MM-dd-yyyy") LocalDate dateFrom,
                                                          @RequestParam(value = "date_to", required = false) @DateTimeFormat(pattern="MM-dd-yyyy")  LocalDate dateTo,
                                                          @RequestParam(value = "campaign", required = false) String campaignFilter,
                                                          @RequestParam(value = "datasource", required = false) String datasourceFilter) {
        List<CtrDTO> result = dataAccessService.ctr(groupBy, dateFrom, dateTo, campaignFilter, datasourceFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/impressions_over_time", method = RequestMethod.GET)
    public ResponseEntity<List<ImpressionsOverTimeDTO>> getImpressionsOverTime(@RequestParam(value = "group_by", required = false) List<String> groupBy,
                                                               @RequestParam(value = "date_from", required = false) @DateTimeFormat(pattern="MM-dd-yyyy") LocalDate dateFrom,
                                                               @RequestParam(value = "date_to", required = false) @DateTimeFormat(pattern="MM-dd-yyyy")  LocalDate dateTo,
                                                               @RequestParam(value = "campaign", required = false) String campaignFilter,
                                                               @RequestParam(value = "datasource", required = false) String datasourceFilter) {
        List<ImpressionsOverTimeDTO> result = dataAccessService.impressionsOverTime(groupBy, dateFrom, dateTo, campaignFilter, datasourceFilter);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
