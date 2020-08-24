package com.eightlightyears.sjc.service.data_import;

import com.eightlightyears.sjc.model.entities.AdProvider;
import com.eightlightyears.sjc.model.entities.Campaign;
import com.eightlightyears.sjc.model.entities.DailyReport;
import com.eightlightyears.sjc.model.repositories.AdProviderRepository;
import com.eightlightyears.sjc.model.repositories.CampaignRepository;
import com.eightlightyears.sjc.model.repositories.DailyReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CSVDataPersistenceImpl implements CSVDataPersistence {

    @Autowired
    AdProviderRepository adProviderRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    DailyReportRepository dailyReportRepository;

    @Override
    @Transactional
    public void persist(List<CampaignDailyReportCSVRecord> records) {
        Collection<Campaign> campaigns = persistCampaigns(records);
        Collection<AdProvider> adProviders = persistAdProviders(records);
        persistDailyReports(records, campaigns, adProviders);

    }

    Collection<Campaign> persistCampaigns(List<CampaignDailyReportCSVRecord> records) {
        Set<String> campaigns = records.stream().map(r -> r.getCampaign()).collect(Collectors.toSet());
        List<Campaign> alreadySaved = campaignRepository.findByNameIn(campaigns);
        Set<String> alreadySavedNames = alreadySaved.stream()
                .map(c -> c.getName())
                .collect(Collectors.toSet());

        Set<String> notSaved = campaigns;
        notSaved.removeAll(alreadySavedNames);

        Set<Campaign> entitiesToSave = notSaved.stream().map(cn -> Campaign.builder().name(cn).build()).collect(Collectors.toSet());
        List<Campaign> saved = campaignRepository.saveAll(entitiesToSave);

        List<Campaign> result = new ArrayList<>();
        result.addAll(alreadySaved);
        result.addAll(saved);

        return result;
    }

    Collection<AdProvider> persistAdProviders(List<CampaignDailyReportCSVRecord> records) {
        Set<String> adProviders = records.stream().map(r -> r.getDatasource()).collect(Collectors.toSet());
        List<AdProvider> alreadySaved = adProviderRepository.findByNameIn(adProviders);
        Set<String> alreadySavedNames = alreadySaved.stream()
                .map(c -> c.getName())
                .collect(Collectors.toSet());

        Set<String> notSaved = adProviders;
        notSaved.removeAll(alreadySavedNames);

        Set<AdProvider> entitiesToSave = notSaved.stream().map(cn -> AdProvider.builder().name(cn).build()).collect(Collectors.toSet());
        List<AdProvider> saved = adProviderRepository.saveAll(entitiesToSave);

        List<AdProvider> result = new ArrayList<>();
        result.addAll(alreadySaved);
        result.addAll(saved);

        return result;
    }

    void persistDailyReports(Collection<CampaignDailyReportCSVRecord> records, 
                             Collection<Campaign> campaigns, 
                             Collection<AdProvider> adProviders) {
        final Map<String, Campaign> campaignsMap = campaigns.stream().collect(Collectors.toMap(c -> c.getName(), c -> c));
        final Map<String, AdProvider> adProvidersMap = adProviders.stream().collect(Collectors.toMap(ap -> ap.getName(), ap -> ap));

        List<DailyReport> toSave = records.stream().map(r ->
                DailyReport.builder()
                        .adProvider(adProvidersMap.get(r.getDatasource()))
                        .campaign(campaignsMap.get(r.getCampaign()))
                        .daily(r.getDaily())
                        .clicks(r.getClicks())
                        .impressions(r.getImpressions())
                        .build())
                .collect(Collectors.toList());

        dailyReportRepository.saveAll(toSave);


    }


}
