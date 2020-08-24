package com.eightlightyears.sjc.service.data_import;

import com.eightlightyears.sjc.model.entities.Campaign;
import com.eightlightyears.sjc.model.repositories.CampaignRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CSVDataPersistenceImplCampaignsPersistTest {
    @InjectMocks
    CSVDataPersistenceImpl csvDataPersistenceImpl;

    @Mock
    CampaignRepository campaignRepository;

    @Captor
    private ArgumentCaptor<Set<Campaign>> captor;

    @Test
    void campaignsPersistTest() {
        List<CampaignDailyReportCSVRecord> records = Arrays.asList(
                    CampaignDailyReportCSVRecord.builder().campaign("A").build(),
                    CampaignDailyReportCSVRecord.builder().campaign("A").build(),
                    CampaignDailyReportCSVRecord.builder().campaign("B").build(),
                    CampaignDailyReportCSVRecord.builder().campaign("B").build(),
                    CampaignDailyReportCSVRecord.builder().campaign("C").build(),
                    CampaignDailyReportCSVRecord.builder().campaign("D").build());

        when(campaignRepository.findByNameIn(new HashSet<String>(Arrays.asList("A", "B", "C", "D")))).thenReturn(
                Arrays.asList(
                        new Campaign(1L, "B"),
                        new Campaign(2L, "D"))
        );

        when(campaignRepository.saveAll(new HashSet<Campaign>(Arrays.asList(new Campaign(null, "A"),
                                                                            new Campaign(null, "C")))))
                .thenReturn(Arrays.asList(new Campaign(3L, "A"),
                                          new Campaign(4L, "C")));


        Collection<Campaign> campaigns = csvDataPersistenceImpl.persistCampaigns(records);

        verify(campaignRepository, times(1)).saveAll(captor.capture());

        assertThat(captor.getValue())
                .hasSize(2)
                .extracting(Campaign::getName).contains("A", "C");

        assertThat(campaigns)
                .hasSize(4)
                .extracting(Campaign::getName).contains("A", "B", "C", "D");

        assertThat(campaigns).filteredOn(c -> c.getName().contains("A")).containsExactly(new Campaign(3L, "A"));
        assertThat(campaigns).filteredOn(c -> c.getName().contains("C")).containsExactly(new Campaign(4L, "C"));



    }
}
