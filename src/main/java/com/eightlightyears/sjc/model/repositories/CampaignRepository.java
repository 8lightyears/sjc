package com.eightlightyears.sjc.model.repositories;

import com.eightlightyears.sjc.model.entities.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    List<Campaign> findByNameIn(Collection<String> campaignNames);
}
