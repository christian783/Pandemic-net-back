package com.app.pandemicnet.repository;

import com.app.pandemicnet.model.StatusReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StatusReportRepository extends JpaRepository<StatusReport, Long> {
    @Query("SELECT sr FROM StatusReport sr WHERE " +
            "(:status IS NULL OR sr.status = :status) AND " +
            "sr.createdAt BETWEEN :start AND :end AND " +
            "(:location IS NULL OR sr.location = :location)")
    List<StatusReport> findByStatusAndCreatedAtBetweenAndLocation(
            @Param("status") String status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("location") String location);
}