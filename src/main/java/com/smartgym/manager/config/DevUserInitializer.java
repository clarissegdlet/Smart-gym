package com.smartgym.manager.config;

import com.smartgym.manager.domain.Authority;
import com.smartgym.manager.domain.User;
import com.smartgym.manager.repository.AuthorityRepository;
import com.smartgym.manager.repository.UserRepository;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DevUserInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public DevUserInitializer(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        Optional<User> adminOpt = userRepository.findOneByLogin("admin");

        if (adminOpt.isPresent()) {
            User admin = adminOpt.get();

            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setActivated(true);

            Set<Authority> authorities = new HashSet<>();
            authorityRepository.findById("ROLE_ADMIN").ifPresent(authorities::add);
            authorityRepository.findById("ROLE_USER").ifPresent(authorities::add);
            admin.setAuthorities(authorities);

            admin.setResetKey(null);
            admin.setResetDate(null);
            admin.setActivationKey(null);
            admin.setCreatedDate(Instant.now());

            userRepository.save(admin);

            System.out.println("=== ADMIN RESET: login=admin password=admin ===");
        }
    }
}
