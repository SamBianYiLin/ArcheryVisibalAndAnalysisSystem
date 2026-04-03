package com.archery.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "athlete")
public class Athlete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "athlete_no", unique = true, nullable = false)
    private String athleteNo;

    @Column(nullable = false)
    private String name;

    private String gender;
    private Integer age;
    private Double height;
    private Double armSpan;

    @Column(name = "level_name")
    private String levelName;

    private String remark;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}