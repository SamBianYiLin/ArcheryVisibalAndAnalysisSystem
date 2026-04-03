package com.archery.controller;

import com.archery.entity.Athlete;
import com.archery.service.AthleteService;
import com.archery.service.AnalysisService;
import com.archery.service.UserAccountService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {

    private final AthleteService athleteService;
    private final AnalysisService analysisService;
    private final UserAccountService userAccountService;

    public AnalysisController(AthleteService athleteService,
                              AnalysisService analysisService,
                              UserAccountService userAccountService) {
        this.athleteService = athleteService;
        this.analysisService = analysisService;
        this.userAccountService = userAccountService;
    }

    @GetMapping
    public String analysisSelectPage(Authentication authentication, Model model) {
        if (!userAccountService.isAdmin(authentication)) {
            Long athleteId = userAccountService.currentAthleteId(authentication);
            if (athleteId == null) {
                throw new AccessDeniedException("未绑定运动员账号");
            }
            return "redirect:/analysis/" + athleteId;
        }

        model.addAttribute("athletes", athleteService.findAll());
        return "analysis_select";
    }

    @GetMapping("/{athleteId}")
    public String analysisPage(@PathVariable Long athleteId, Authentication authentication, Model model) {
        validateAthleteAccess(authentication, athleteId);

        Athlete athlete = athleteService.findById(athleteId)
                .orElseThrow(() -> new RuntimeException("运动员不存在"));

        Map<String, Object> analysisData = analysisService.getAnalysisByAthleteId(athleteId);

        model.addAttribute("athlete", athlete);
        model.addAttribute("analysisData", analysisData);
        model.addAttribute("isAdmin", userAccountService.isAdmin(authentication));
        return "analysis";
    }

    private void validateAthleteAccess(Authentication authentication, Long targetAthleteId) {
        if (userAccountService.isAdmin(authentication)) {
            return;
        }

        Long currentAthleteId = userAccountService.currentAthleteId(authentication);
        if (currentAthleteId == null || !currentAthleteId.equals(targetAthleteId)) {
            throw new AccessDeniedException("无权访问其他运动员分析");
        }
    }
}