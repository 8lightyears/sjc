package com.eightlightyears.sjc.service.data_access;

import com.eightlightyears.sjc.model.entities.AdProvider;
import com.eightlightyears.sjc.model.entities.Campaign;
import com.eightlightyears.sjc.model.entities.DailyReport;
import com.eightlightyears.sjc.model.repositories.AdProviderRepository;
import com.eightlightyears.sjc.model.repositories.CampaignRepository;
import com.eightlightyears.sjc.model.repositories.DailyReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class DataAccessServiceImplTest {

    @Autowired
    EntityManager entityManager;

    @InjectMocks
    DataAccessServiceImpl dataAccessServiceImpl;

    @Autowired
    AdProviderRepository adProviderRepository;

    @Autowired
    CampaignRepository campaignRepository;

    @Autowired
    DailyReportRepository dailyReportRepository;



    private void createTestData() {
        List<AdProvider> adProviders = adProviderRepository.saveAll(Arrays.asList(
                new AdProvider(1L, "AP1"),
                new AdProvider(2L, "AP2"),
                new AdProvider(3L, "AP3")));

        List<Campaign> campaigns = campaignRepository.saveAll(Arrays.asList(
                new Campaign(11L, "C1"),
                new Campaign(12L, "C2"),
                new Campaign(13L, "C3")));
        adProviderRepository.flush();
        campaignRepository.flush();



        List<DailyReport> drs = Arrays.asList(
                new DailyReport(null, adProviders.get(0), campaigns.get(0), LocalDate.of(2020, 8, 1), 10L, 100L),
                new DailyReport(null, adProviders.get(0), campaigns.get(1), LocalDate.of(2020, 8, 2), 11L, 101L),

                new DailyReport(null, adProviders.get(1), campaigns.get(0), LocalDate.of(2020, 8, 3), 10L, 100L),
                new DailyReport(null, adProviders.get(1), campaigns.get(1), LocalDate.of(2020, 8, 4), 11L, 101L)

                );
        dailyReportRepository.saveAll(drs);

    }

    @Test
    void filteringTest() {
        assertThat(entityManager).isNotNull();
        dataAccessServiceImpl.em = entityManager;


        createTestData();
        List<DailyReport> result = dataAccessServiceImpl.filter(null, null, null, null);
        assertThat(result).hasSize(4);

        result = dataAccessServiceImpl.filter(
                LocalDate.of(2020, 8, 2),
                LocalDate.of(2020, 8, 2),
                null,
                null);
        assertThat(result).hasSize(1);

        result = dataAccessServiceImpl.filter(
                LocalDate.of(2020, 8, 1),
                LocalDate.of(2020, 8, 3),
                null,
                null);
        assertThat(result).hasSize(3);

        result = dataAccessServiceImpl.filter(
                null,
                null,
                "C2",
                null);
        assertThat(result).hasSize(2);

        result = dataAccessServiceImpl.filter(
                null,
                null,
                "C4",
                null);
        assertThat(result).hasSize(0);

        result = dataAccessServiceImpl.filter(
                null,
                null,
                null,
                "AP1");
        assertThat(result).hasSize(2);

        result = dataAccessServiceImpl.filter(
                LocalDate.of(2020, 8, 1),
                LocalDate.of(2020, 8, 4),
                "C2",
                "AP2");
        assertThat(result).hasSize(1);

    }

    @Test
    void createGroupsTest() {
        createTestData();
        List<DailyReport> testData = dailyReportRepository.findAll();

        Collection<DataGroup> groups = dataAccessServiceImpl.group(Arrays.asList("datasource"), testData);
        assertThat(groups).hasSize(2);
        assertThat(groups).filteredOn(g -> g.getGroup().getName().equals("AP1")).hasSize(1);

        groups = dataAccessServiceImpl.group(Arrays.asList("datasource", "campaign"), testData);
        assertThat(groups).hasSize(4);

        groups = dataAccessServiceImpl.group(Arrays.asList(""), testData);
        assertThat(groups).hasSize(1);

        groups = dataAccessServiceImpl.group(null, testData);
        assertThat(groups).hasSize(1);

    }
}
