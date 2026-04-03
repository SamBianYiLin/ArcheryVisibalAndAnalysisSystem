package com.archery.repository;

import com.archery.entity.Athlete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AthleteRepository extends JpaRepository<Athlete, Long> {

	@Query(value = "SELECT MAX(CAST(SUBSTRING(athlete_no, 4) AS UNSIGNED)) FROM athlete", nativeQuery = true)
	Long findMaxAthleteNoSeq();
}