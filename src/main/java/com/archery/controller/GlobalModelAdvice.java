package com.archery.controller;

import com.archery.entity.Athlete;
import com.archery.service.AthleteService;
import com.archery.service.UserAccountService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    private final UserAccountService userAccountService;
    private final AthleteService athleteService;

    public GlobalModelAdvice(UserAccountService userAccountService, AthleteService athleteService) {
        this.userAccountService = userAccountService;
        this.athleteService = athleteService;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication authentication) {
        return userAccountService.isAdmin(authentication);
    }

    @ModelAttribute("currentAthleteName")
    public String currentAthleteName(Authentication authentication) {
        Long athleteId = userAccountService.currentAthleteId(authentication);
        if (athleteId == null) {
            return null;
        }
        return athleteService.findById(athleteId)
                .map(Athlete::getName)
                .orElse(null);
    }

    @ModelAttribute("currentAthleteId")
    public Long currentAthleteId(Authentication authentication) {
        return userAccountService.currentAthleteId(authentication);
    }
}

