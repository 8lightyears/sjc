package com.eightlightyears.sjc.model.repositories;

import com.eightlightyears.sjc.model.entities.DailyReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyReportRepository extends JpaRepository<DailyReport, Long> {

}
