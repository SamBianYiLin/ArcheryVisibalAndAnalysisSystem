package com.archery.controller;

import com.archery.entity.Athlete;
import com.archery.service.AthleteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/athletes")
public class AthleteController {

    private final AthleteService athleteService;

    public AthleteController(AthleteService athleteService) {
        this.athleteService = athleteService;
    }

    @GetMapping
    public String listAthletes(@RequestParam(value = "success", required = false) String success, Model model) {
        model.addAttribute("athletes", athleteService.findAll());
        model.addAttribute("athlete", new Athlete());
        model.addAttribute("success", success);
        return "athletes";
    }

    @PostMapping("/add")
    public String addAthlete(@ModelAttribute Athlete athlete) {
        athleteService.save(athlete);
        return "redirect:/athletes?success=added";
    }

    @GetMapping("/delete/{id}")
    public String deleteAthlete(@PathVariable Long id) {
        athleteService.deleteById(id);
        return "redirect:/athletes";
    }
}