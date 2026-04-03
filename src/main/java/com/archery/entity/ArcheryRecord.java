package com.archery.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "archery_record")
public class ArcheryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "athlete_id", nullable = false)
    private Long athleteId;

    @Column(nullable = false)
    private Integer score;

    @Column(name = "main_muscle")
    private String mainMuscle;

    @Column(name = "synergy_muscles")
    private String synergyMuscles;

    @Lob
    @Column(name = "raw_json", columnDefinition = "TEXT")
    private String rawJson;

    private String remark;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}