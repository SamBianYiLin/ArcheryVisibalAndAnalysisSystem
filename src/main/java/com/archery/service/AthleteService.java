package com.archery.service;

import com.archery.entity.Athlete;
import com.archery.repository.ArcheryRecordRepository;
import com.archery.repository.AthleteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AthleteService {

    private final AthleteRepository athleteRepository;
    private final ArcheryRecordRepository archeryRecordRepository;
    private final UserAccountService userAccountService;

    public AthleteService(AthleteRepository athleteRepository,
                          ArcheryRecordRepository archeryRecordRepository,
                          UserAccountService userAccountService) {
        this.athleteRepository = athleteRepository;
        this.archeryRecordRepository = archeryRecordRepository;
        this.userAccountService = userAccountService;
    }

    public List<Athlete> findAll() {
        return athleteRepository.findAll();
    }

    public Athlete save(Athlete athlete) {
        if (athlete.getId() == null && (athlete.getAthleteNo() == null || athlete.getAthleteNo().isBlank())) {
            Long maxSeq = athleteRepository.findMaxAthleteNoSeq();
            long nextSeq = maxSeq == null ? 1 : maxSeq + 1;
            String athleteNo = String.format("ATH%04d", nextSeq);
            athlete.setAthleteNo(athleteNo);
        }
        return athleteRepository.save(athlete);
    }

    @Transactional
    public void deleteById(Long id) {
        archeryRecordRepository.deleteByAthleteId(id);
        userAccountService.deleteByAthleteId(id);
        athleteRepository.deleteById(id);
    }

    public Optional<Athlete> findById(Long id) {
        return athleteRepository.findById(id);
    }
}