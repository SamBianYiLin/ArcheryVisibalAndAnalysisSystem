package com.archery.service;

import com.archery.entity.Athlete;
import com.archery.repository.AthleteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AthleteService {

    private final AthleteRepository athleteRepository;

    public AthleteService(AthleteRepository athleteRepository) {
        this.athleteRepository = athleteRepository;
    }

    public List<Athlete> findAll() {
        return athleteRepository.findAll();
    }

    public Athlete save(Athlete athlete) {
        // 如果是新增（没有ID），自动生成编号
        if (athlete.getId() == null) {
            long count = athleteRepository.count() + 1;
            String athleteNo = String.format("ATH%04d", count);
            athlete.setAthleteNo(athleteNo);
        }
        return athleteRepository.save(athlete);
    }

    public void deleteById(Long id) {
        athleteRepository.deleteById(id);
    }

    public Optional<Athlete> findById(Long id) {
        return athleteRepository.findById(id);
    }
}