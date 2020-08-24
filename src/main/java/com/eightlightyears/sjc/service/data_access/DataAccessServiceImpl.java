package com.eightlightyears.sjc.service.data_access;


import com.eightlightyears.sjc.model.entities.DailyReport;
import com.eightlightyears.sjc.service.data_access.dto.CtrDTO;
import com.eightlightyears.sjc.service.data_access.dto.ImpressionsOverTimeDTO;
import com.eightlightyears.sjc.service.data_access.dto.TotalClicksDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataAccessServiceImpl implements DataAccessService {

    @Autowired
    EntityManager em;

    @Override
    public List<TotalClicksDTO> totalClicks(List<String> groupBy,
                                            LocalDate dateFrom,
                                            LocalDate dateTo,
                                            String campaignFilter,
                                            String datasourceFilter) {
        List<DailyReport> filtered = filter(dateFrom, dateTo, campaignFilter, datasourceFilter);
        List<DataGroup> groups = group(groupBy, filtered);
        sortGroups(groups);

        List<TotalClicksDTO> result = groups.stream().map(g -> {
            List<Object> group = new ArrayList<>();
            if (g.getGroup().getFields() != null)
                group.addAll(g.getGroup().getFields());
            LongSummaryStatistics sum = g.getDailyReportList().stream().collect(Collectors.summarizingLong(DailyReport::getClicks));
            TotalClicksDTO record = new TotalClicksDTO(group, sum.getSum());
            return record;
        }).collect(Collectors.toList());

        return result;
    }

    @Override
    public List<CtrDTO> ctr(List<String> groupBy,
                            LocalDate dateFrom,
                            LocalDate dateTo,
                            String campaignFilter,
                            String datasourceFilter) {
        List<DailyReport> filtered = filter(dateFrom, dateTo, campaignFilter, datasourceFilter);
        List<DataGroup> groups = group(groupBy, filtered);
        sortGroups(groups);

        List<CtrDTO> result = groups.stream().map(g -> {
            List<Object> group = new ArrayList<>();
            if (g.getGroup().getFields() != null)
                group.addAll(g.getGroup().getFields());
            Long sumClicks = g.getDailyReportList().stream().collect(Collectors.summarizingLong(DailyReport::getClicks)).getSum();
            Long sumImpressions = g.getDailyReportList().stream().collect(Collectors.summarizingLong(DailyReport::getImpressions)).getSum();
            CtrDTO record = new CtrDTO(group, sumClicks.doubleValue() / sumImpressions.doubleValue() * 100.0);
            return record;
        }).collect(Collectors.toList());

        return result;
    }

    @Override
    public List<ImpressionsOverTimeDTO> impressionsOverTime(List<String> groupBy, LocalDate dateFrom, LocalDate dateTo, String campaignFilter, String datasourceFilter) {
        List<DailyReport> filtered = filter(dateFrom, dateTo, campaignFilter, datasourceFilter);
        List<DataGroup> groups = group(groupBy, filtered);
        sortGroups(groups);

        List<ImpressionsOverTimeDTO> result = groups.stream().map(g -> {
            List<Object> group = new ArrayList<>();
            if (g.getGroup().getFields() != null)
                group.addAll(g.getGroup().getFields());
            g.getDailyReportList().sort(Comparator.comparing(DailyReport::getDaily));
            long sum = 0L;
            List<ImpressionsOverTimeDTO.DailyImpressionsOverTimeDTO> dailyImpressionsOverTimeList = new ArrayList<>();
            LocalDate lastDate = null;
            ImpressionsOverTimeDTO.DailyImpressionsOverTimeDTO current = null;
            for (DailyReport dr : g.getDailyReportList()) {
                sum += dr.getImpressions();
                if (!dr.getDaily().equals(lastDate) || dr.equals(g.getDailyReportList().get(g.getDailyReportList().size() - 1))) {
                    if (current != null)
                        dailyImpressionsOverTimeList.add(current);
                    current = new ImpressionsOverTimeDTO.DailyImpressionsOverTimeDTO(dr.getDaily(), sum);
                } else {
                    current.setValue(sum);
                }
                lastDate = dr.getDaily();
            }

            ImpressionsOverTimeDTO record = new ImpressionsOverTimeDTO(group, dailyImpressionsOverTimeList);
            return record;
        }).collect(Collectors.toList());

        return result;
    }

    List<DailyReport> filter(LocalDate dateFrom, LocalDate dateTo, String campaignFilter, String datasourceFilter) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<DailyReport> query = builder.createQuery(DailyReport.class);

        Root<DailyReport> root = query.from(DailyReport.class);
        List<Predicate> predicates = new ArrayList<>();
        if (dateFrom != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("daily"), dateFrom));
        }
        if (dateTo != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("daily"), dateTo));
        }
        if (campaignFilter != null) {
            predicates.add(builder.equal(root.join("campaign").get("name"), campaignFilter));
        }
        if (datasourceFilter != null) {
            predicates.add(builder.equal(root.join("adProvider").get("name"), datasourceFilter));
        }
        query.where(builder.and(predicates.toArray(new Predicate[0])));

        List<DailyReport> result = em.createQuery(query.select(root)).getResultList();
        return result;
    }



    List<DataGroup> group(List<String> groupBy, List<DailyReport> data) {
        Map<String, DataGroup> groups = new HashMap<>();
        if (groupBy == null) {
            groups.put("", new DataGroup(new DataGroupDesc(null), data));
        } else {
            for (DailyReport record : data) {
                DataGroupDesc dataGroupDesc = calcGroup(record, groupBy);
                String dataGroupName = dataGroupDesc.getName();
                DataGroup dataGroup = groups.getOrDefault(dataGroupName, new DataGroup(dataGroupDesc, new ArrayList<DailyReport>()));
                dataGroup.getDailyReportList().add(record);
                groups.put(dataGroupName, dataGroup);
            }
        }
        ArrayList<DataGroup> result = new ArrayList<DataGroup>();
        result.addAll(groups.values());
        return result;
    }

    DataGroupDesc calcGroup(DailyReport record, List<String> groupBy) {
        List<Comparable> fields = new ArrayList<>();
        for (String group : groupBy) {
            if (group.equals("datasource")) {
                fields.add(record.getAdProvider().getName());
            } else if (group.equals("campaign")) {
                fields.add(record.getCampaign().getName());
            }
        }
        DataGroupDesc result = new DataGroupDesc(fields);
        return result;
    }


    private void sortGroups(List<DataGroup> groups) {
        Collections.sort(groups);
    }

}
