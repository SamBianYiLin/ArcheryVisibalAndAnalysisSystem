package com.archery.repository;

import com.archery.entity.ArcheryRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArcheryRecordRepository extends JpaRepository<ArcheryRecord, Long> {
    List<ArcheryRecord> findByAthleteIdOrderByCreatedAtDesc(Long athleteId);

    void deleteByAthleteId(Long athleteId);
}