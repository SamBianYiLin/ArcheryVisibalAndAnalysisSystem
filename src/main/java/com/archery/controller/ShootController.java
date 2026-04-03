package com.archery.controller;

import com.archery.entity.Athlete;
import com.archery.service.AthleteService;
import com.archery.service.ShootService;
import com.archery.service.UserAccountService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/shoot")
public class ShootController {

    private final AthleteService athleteService;
    private final ShootService shootService;
    private final UserAccountService userAccountService;

    public ShootController(AthleteService athleteService, ShootService shootService, UserAccountService userAccountService) {
        this.athleteService = athleteService;
        this.shootService = shootService;
        this.userAccountService = userAccountService;
    }

    @GetMapping("/{athleteId}")
    public String shootPage(@PathVariable Long athleteId, Authentication authentication, Model model) {
        validateAthleteAccess(authentication, athleteId);

        Athlete athlete = athleteService.findById(athleteId)
                .orElseThrow(() -> new RuntimeException("运动员不存在"));

        model.addAttribute("athlete", athlete);
        model.addAttribute("historyList", shootService.getHistoryByAthleteId(athleteId));
        model.addAttribute("backUrl", userAccountService.isAdmin(authentication) ? "/athletes" : "/");
        return "shoot";
    }

    @PostMapping("/simulate/{athleteId}")
    @ResponseBody
    public Map<String, Object> simulateShoot(@PathVariable Long athleteId, Authentication authentication) {
        validateAthleteAccess(authentication, athleteId);
        return shootService.simulateShoot(athleteId);
    }

    @PostMapping("/save/{athleteId}")
    @ResponseBody
    public String saveShoot(@PathVariable Long athleteId,
                            Authentication authentication,
                            @RequestBody Map<String, Object> result) {
        validateAthleteAccess(authentication, athleteId);
        shootService.saveShootResult(athleteId, result);
        return "保存成功";
    }

    private void validateAthleteAccess(Authentication authentication, Long targetAthleteId) {
        if (userAccountService.isAdmin(authentication)) {
            return;
        }

        Long currentAthleteId = userAccountService.currentAthleteId(authentication);
        if (currentAthleteId == null || !currentAthleteId.equals(targetAthleteId)) {
            throw new AccessDeniedException("无权访问其他运动员数据");
        }
    }
}