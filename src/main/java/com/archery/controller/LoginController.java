package com.archery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "registered", required = false) String registered,
                            @RequestParam(value = "portal", required = false) String portalError,
                            Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        model.addAttribute("registered", registered != null);
        model.addAttribute("portalMismatch", portalError != null);
        return "login";
    }

    @GetMapping("/admin/login")
    public String adminLoginPage(@RequestParam(value = "error", required = false) String error,
                                 @RequestParam(value = "portal", required = false) String portalError,
                                 Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("portalMismatch", portalError != null);
        return "admin_login";
    }
}

