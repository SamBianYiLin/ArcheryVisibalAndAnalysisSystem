package com.archery.service;

import com.archery.entity.UserAccount;
import com.archery.repository.UserAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserAccountService {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9]+$");

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserAccount> findByUsername(String username) {
        return userAccountRepository.findByUsername(username);
    }

    public boolean isUsernameValid(String username) {
        return username != null && USERNAME_PATTERN.matcher(username).matches();
    }

    public boolean isUsernameExists(String username) {
        return userAccountRepository.existsByUsername(username);
    }

    public UserAccount createAthleteAccount(String username, String rawPassword, Long athleteId) {
        UserAccount account = new UserAccount();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(rawPassword));
        account.setRole("ATHLETE");
        account.setAthleteId(athleteId);
        return userAccountRepository.save(account);
    }

    public boolean isAdmin(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }

    public boolean isAthlete(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream().anyMatch(a -> "ROLE_ATHLETE".equals(a.getAuthority()));
    }

    public Long currentAthleteId(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        return findByUsername(authentication.getName())
                .map(UserAccount::getAthleteId)
                .orElse(null);
    }

    public boolean verifyPassword(UserAccount account, String rawPassword) {
        return passwordEncoder.matches(rawPassword, account.getPassword());
    }

    public Optional<UserAccount> currentAccount(Authentication authentication) {
        if (authentication == null) {
            return Optional.empty();
        }
        return findByUsername(authentication.getName());
    }

    public void updatePassword(UserAccount account, String newRawPassword) {
        account.setPassword(passwordEncoder.encode(newRawPassword));
        userAccountRepository.save(account);
    }

    public void deleteByAthleteId(Long athleteId) {
        userAccountRepository.deleteByAthleteId(athleteId);
    }

    public long countAdminAccounts() {
        return userAccountRepository.countByRole("ADMIN");
    }
}


