package com.eightlightyears.sjc.service.data_import;

import com.eightlightyears.sjc.model.entities.AdProvider;
import com.eightlightyears.sjc.model.repositories.AdProviderRepository;
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
public class CSVDataPersistenceImplAdProviderPersistTest {
    @InjectMocks
    CSVDataPersistenceImpl csvDataPersistenceImpl;

    @Mock
    AdProviderRepository adProviderRepository;

    @Captor
    private ArgumentCaptor<Set<AdProvider>> captor;

    @Test
    void campaignsPersistTest() {
        List<CampaignDailyReportCSVRecord> records = Arrays.asList(
                    CampaignDailyReportCSVRecord.builder().datasource("A").build(),
                    CampaignDailyReportCSVRecord.builder().datasource("A").build(),
                    CampaignDailyReportCSVRecord.builder().datasource("B").build(),
                    CampaignDailyReportCSVRecord.builder().datasource("B").build(),
                    CampaignDailyReportCSVRecord.builder().datasource("C").build(),
                    CampaignDailyReportCSVRecord.builder().datasource("D").build());

        when(adProviderRepository.findByNameIn(new HashSet<String>(Arrays.asList("A", "B", "C", "D")))).thenReturn(
                Arrays.asList(
                        new AdProvider(1L, "B"),
                        new AdProvider(2L, "D"))
        );

        when(adProviderRepository.saveAll(new HashSet<AdProvider>(Arrays.asList(new AdProvider(null, "A"),
                                                                                new AdProvider(null, "C")))))
                .thenReturn(Arrays.asList(new AdProvider(3L, "A"),
                                          new AdProvider(4L, "C")));


        Collection<AdProvider> adProviders = csvDataPersistenceImpl.persistAdProviders(records);

        verify(adProviderRepository, times(1)).saveAll(captor.capture());

        assertThat(captor.getValue())
                .hasSize(2)
                .extracting(AdProvider::getName).contains("A", "C");

        assertThat(adProviders)
                .hasSize(4)
                .extracting(AdProvider::getName).contains("A", "B", "C", "D");

        assertThat(adProviders).filteredOn(ap -> ap.getName().contains("A")).containsExactly(new AdProvider(3L, "A"));
        assertThat(adProviders).filteredOn(ap -> ap.getName().contains("C")).containsExactly(new AdProvider(4L, "C"));



    }
}
