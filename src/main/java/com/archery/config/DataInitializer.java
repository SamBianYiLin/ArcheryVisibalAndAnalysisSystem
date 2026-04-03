package com.archery.config;

import com.archery.entity.UserAccount;
import com.archery.repository.UserAccountRepository;
import com.archery.service.UserAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserAccountRepository userAccountRepository,
                                       UserAccountService userAccountService,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            long adminCount = userAccountService.countAdminAccounts();
            if (adminCount > 1) {
                throw new IllegalStateException("系统仅允许一个管理员账号，请清理重复管理员数据");
            }

            if (!userAccountRepository.existsByUsername("admin")) {
                UserAccount admin = new UserAccount();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                userAccountRepository.save(admin);
            }
        };
    }
}

