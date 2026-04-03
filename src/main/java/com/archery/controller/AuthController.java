package com.archery.controller;

import com.archery.entity.Athlete;
import com.archery.entity.UserAccount;
import com.archery.service.AthleteService;
import com.archery.service.UserAccountService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserAccountService userAccountService;
    private final AthleteService athleteService;

    public AuthController(UserAccountService userAccountService, AthleteService athleteService) {
        this.userAccountService = userAccountService;
        this.athleteService = athleteService;
    }

    @GetMapping("/register")
    public String registerPage(Model model,
                               @RequestParam(value = "error", required = false) String error,
                               @RequestParam(value = "success", required = false) String success) {
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           @RequestParam String name,
                           @RequestParam(required = false) String gender,
                           @RequestParam(required = false) Integer age,
                           @RequestParam(required = false) Double height,
                           @RequestParam(required = false) Double armSpan,
                           @RequestParam(required = false) String levelName,
                           @RequestParam(required = false) String remark) {
        if ("admin".equalsIgnoreCase(username)) {
            return "redirect:/auth/register?error=该账号名为系统保留账号";
        }

        if (!userAccountService.isUsernameValid(username)) {
            return "redirect:/auth/register?error=账号只能由字母和数字组成";
        }

        if (userAccountService.isUsernameExists(username)) {
            return "redirect:/auth/register?error=账号已存在";
        }

        if (password == null || password.length() < 6) {
            return "redirect:/auth/register?error=密码长度至少6位";
        }

        if (!password.equals(confirmPassword)) {
            return "redirect:/auth/register?error=两次输入的密码不一致";
        }

        Athlete athlete = new Athlete();
        athlete.setName(name);
        athlete.setGender(gender);
        athlete.setAge(age);
        athlete.setHeight(height);
        athlete.setArmSpan(armSpan);
        athlete.setLevelName(levelName);
        athlete.setRemark(remark);

        Athlete savedAthlete = athleteService.save(athlete);
        userAccountService.createAthleteAccount(username, password, savedAthlete.getId());

        return "redirect:/login?registered";
    }

    @GetMapping("/password")
    public String changePasswordPage(Model model,
                                     @RequestParam(value = "error", required = false) String error,
                                     @RequestParam(value = "success", required = false) String success) {
        model.addAttribute("error", error);
        model.addAttribute("success", success);
        return "password";
    }

    @PostMapping("/password")
    public String changePassword(Authentication authentication,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword) {
        UserAccount account = userAccountService.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("账号不存在"));

        if (!userAccountService.verifyPassword(account, currentPassword)) {
            return "redirect:/auth/password?error=当前密码不正确";
        }

        if (newPassword == null || newPassword.length() < 6) {
            return "redirect:/auth/password?error=新密码长度至少6位";
        }

        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/auth/password?error=两次新密码不一致";
        }

        userAccountService.updatePassword(account, newPassword);
        return "redirect:/auth/password?success=密码修改成功";
    }

    @GetMapping("/login")
    public String toLogin() {
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profilePage(Authentication authentication,
                              @RequestParam(value = "success", required = false) String success,
                              Model model) {
        if (userAccountService.isAdmin(authentication)) {
            throw new AccessDeniedException("管理员无需个人运动员档案");
        }

        Long athleteId = userAccountService.currentAthleteId(authentication);
        Athlete athlete = athleteService.findById(athleteId)
                .orElseThrow(() -> new RuntimeException("运动员不存在"));

        model.addAttribute("athlete", athlete);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("success", success);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(Authentication authentication,
                                @RequestParam String name,
                                @RequestParam(required = false) String gender,
                                @RequestParam(required = false) Integer age,
                                @RequestParam(required = false) Double height,
                                @RequestParam(required = false) Double armSpan,
                                @RequestParam(required = false) String levelName,
                                @RequestParam(required = false) String remark) {
        if (userAccountService.isAdmin(authentication)) {
            throw new AccessDeniedException("管理员无需个人运动员档案");
        }

        Long athleteId = userAccountService.currentAthleteId(authentication);
        Athlete athlete = athleteService.findById(athleteId)
                .orElseThrow(() -> new RuntimeException("运动员不存在"));

        athlete.setName(name);
        athlete.setGender(gender);
        athlete.setAge(age);
        athlete.setHeight(height);
        athlete.setArmSpan(armSpan);
        athlete.setLevelName(levelName);
        athlete.setRemark(remark);

        athleteService.save(athlete);
        return "redirect:/auth/profile?success=资料更新成功";
    }
}

