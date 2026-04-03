package com.archery.service;

import com.archery.entity.ArcheryRecord;
import com.archery.repository.ArcheryRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ShootService {

    private final ArcheryRecordRepository archeryRecordRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ShootService(ArcheryRecordRepository archeryRecordRepository) {
        this.archeryRecordRepository = archeryRecordRepository;
    }

    public Map<String, Object> simulateShoot(Long athleteId) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<String> muscles = Arrays.asList(
                "deltoid",           // 三角肌
                "biceps",            // 肱二头肌
                "triceps",           // 肱三头肌
                "forearm_flexors",   // 前臂屈肌群
                "latissimus_dorsi"   // 背阔肌
        );

        Map<String, String> muscleCnMap = new LinkedHashMap<>();
        muscleCnMap.put("deltoid", "三角肌");
        muscleCnMap.put("biceps", "肱二头肌");
        muscleCnMap.put("triceps", "肱三头肌");
        muscleCnMap.put("forearm_flexors", "前臂屈肌群");
        muscleCnMap.put("latissimus_dorsi", "背阔肌");

        Random random = new Random();

        //  此处采用随机生成数据，若之后接入传感器或计算机视觉识别结果，可按照实际结果记录
        int score = 6 + random.nextInt(5); // 6~10环
        String mainMuscle = muscles.get(random.nextInt(muscles.size()));

        Set<String> synergySet = new LinkedHashSet<>();
        while (synergySet.size() < 2) {
            String m = muscles.get(random.nextInt(muscles.size()));
            if (!m.equals(mainMuscle)) {
                synergySet.add(m);
            }
        }

        Map<String, Double> muscleActivation = new LinkedHashMap<>();
        for (String muscle : muscles) {
            double value = 0.20 + random.nextDouble() * 0.60;
            muscleActivation.put(muscle, Math.round(value * 100.0) / 100.0);
        }
        muscleActivation.put(mainMuscle, Math.round((0.85 + random.nextDouble() * 0.10) * 100.0) / 100.0);

        List<String> synergyMuscles = new ArrayList<>(synergySet);

        String analysis = "本次射箭以" + muscleCnMap.get(mainMuscle)
                + "为主要发力肌群，"
                + muscleCnMap.get(synergyMuscles.get(0)) + "、"
                + muscleCnMap.get(synergyMuscles.get(1))
                + "参与协同发力，整体动作较稳定。";

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("athleteId", athleteId);
        result.put("score", score);
        result.put("mainMuscle", mainMuscle);
        result.put("mainMuscleCn", muscleCnMap.get(mainMuscle));
        result.put("synergyMuscles", synergyMuscles);

        List<String> synergyMusclesCn = new ArrayList<>();
        for (String m : synergyMuscles) {
            synergyMusclesCn.add(muscleCnMap.get(m));
        }
        result.put("synergyMusclesCn", synergyMusclesCn);

        result.put("muscleActivation", muscleActivation);
        result.put("analysis", analysis);
        result.put("timestamp", LocalDateTime.now().toString());

        return result;
    }

    public ArcheryRecord saveShootResult(Long athleteId, Map<String, Object> result) {
        try {
            ArcheryRecord record = new ArcheryRecord();
            record.setAthleteId(athleteId);
            record.setScore((Integer) result.get("score"));
            record.setMainMuscle((String) result.get("mainMuscleCn"));

            @SuppressWarnings("unchecked")
            List<String> synergyMusclesCn = (List<String>) result.get("synergyMusclesCn");
            record.setSynergyMuscles(String.join("、", synergyMusclesCn));

            //  保存结果为json文件，后续接入传感器后，可将传感器数据转为json文件以进行数据记录与处理
            record.setRawJson(objectMapper.writeValueAsString(result));
            record.setRemark((String) result.get("analysis"));
            record.setCreatedAt(LocalDateTime.now());

            return archeryRecordRepository.save(record);
        } catch (Exception e) {
            throw new RuntimeException("保存射箭结果失败", e);
        }
    }

    public List<ArcheryRecord> getHistoryByAthleteId(Long athleteId) {
        return archeryRecordRepository.findByAthleteIdOrderByCreatedAtDesc(athleteId);
    }
}