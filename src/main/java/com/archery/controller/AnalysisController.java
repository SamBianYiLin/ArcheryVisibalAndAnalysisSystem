package com.archery.controller;

import com.archery.entity.Athlete;
import com.archery.service.AthleteService;
import com.archery.service.AnalysisService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {

    private final AthleteService athleteService;
    private final AnalysisService analysisService;

    public AnalysisController(AthleteService athleteService, AnalysisService analysisService) {
        this.athleteService = athleteService;
        this.analysisService = analysisService;
    }

    @GetMapping
    public String analysisSelectPage(Model model) {
        model.addAttribute("athletes", athleteService.findAll());
        return "analysis_select";
    }

    @GetMapping("/{athleteId}")
    public String analysisPage(@PathVariable Long athleteId, Model model) {
        Athlete athlete = athleteService.findById(athleteId)
                .orElseThrow(() -> new RuntimeException("运动员不存在"));

        Map<String, Object> analysisData = analysisService.getAnalysisByAthleteId(athleteId);

        model.addAttribute("athlete", athlete);
        model.addAttribute("analysisData", analysisData);
        return "analysis";
    }
}