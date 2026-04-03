package com.archery.controller;

import com.archery.entity.Athlete;
import com.archery.service.AthleteService;
import com.archery.service.ShootService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/shoot")
public class ShootController {

    private final AthleteService athleteService;
    private final ShootService shootService;

    public ShootController(AthleteService athleteService, ShootService shootService) {
        this.athleteService = athleteService;
        this.shootService = shootService;
    }

    @GetMapping("/{athleteId}")
    public String shootPage(@PathVariable Long athleteId, Model model) {
        Athlete athlete = athleteService.findById(athleteId)
                .orElseThrow(() -> new RuntimeException("运动员不存在"));

        model.addAttribute("athlete", athlete);
        model.addAttribute("historyList", shootService.getHistoryByAthleteId(athleteId));
        return "shoot";
    }

    @PostMapping("/simulate/{athleteId}")
    @ResponseBody
    public Map<String, Object> simulateShoot(@PathVariable Long athleteId) {
        return shootService.simulateShoot(athleteId);
    }

    @PostMapping("/save/{athleteId}")
    @ResponseBody
    public String saveShoot(@PathVariable Long athleteId, @RequestBody Map<String, Object> result) {
        shootService.saveShootResult(athleteId, result);
        return "保存成功";
    }
}