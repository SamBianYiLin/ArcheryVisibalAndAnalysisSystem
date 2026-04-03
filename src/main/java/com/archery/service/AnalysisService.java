package com.archery.service;

import com.archery.entity.ArcheryRecord;
import com.archery.repository.ArcheryRecordRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AnalysisService {

    private final ArcheryRecordRepository archeryRecordRepository;

    public AnalysisService(ArcheryRecordRepository archeryRecordRepository) {
        this.archeryRecordRepository = archeryRecordRepository;
    }

    public Map<String, Object> getAnalysisByAthleteId(Long athleteId) {
        List<ArcheryRecord> records = archeryRecordRepository.findByAthleteIdOrderByCreatedAtDesc(athleteId);

        Map<String, Object> result = new HashMap<>();
        result.put("records", records);

        if (records.isEmpty()) {
            result.put("totalCount", 0);
            result.put("avgScore", 0.0);
            result.put("bestScore", 0);
            result.put("tenRate", 0.0);
            result.put("muscleStats", new LinkedHashMap<String, Integer>());
            return result;
        }

        int total = records.size();
        int sum = 0;
        int best = 0;
        int tenCount = 0;

        Map<String, Integer> muscleStats = new LinkedHashMap<>();

        for (ArcheryRecord record : records) {
            int score = record.getScore();
            sum += score;
            if (score > best) {
                best = score;
            }
            if (score == 10) {
                tenCount++;
            }

            String mainMuscle = record.getMainMuscle();
            if (mainMuscle != null && !mainMuscle.isEmpty()) {
                muscleStats.put(mainMuscle, muscleStats.getOrDefault(mainMuscle, 0) + 1);
            }
        }

        double avgScore = (double) sum / total;
        double tenRate = (double) tenCount * 100.0 / total;

        result.put("totalCount", total);
        result.put("avgScore", Math.round(avgScore * 100.0) / 100.0);
        result.put("bestScore", best);
        result.put("tenRate", Math.round(tenRate * 100.0) / 100.0);
        result.put("muscleStats", muscleStats);

        List<Integer> scoreList = records.stream()
                .map(ArcheryRecord::getScore)
                .toList();

        result.put("scoreList", scoreList);

        return result;
    }
}