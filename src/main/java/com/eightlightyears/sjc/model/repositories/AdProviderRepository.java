package com.eightlightyears.sjc.model.repositories;

import com.eightlightyears.sjc.model.entities.AdProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface AdProviderRepository extends JpaRepository<AdProvider, Long> {

    List<AdProvider> findByNameIn(Set<String> adProviders);
}
