package com.exam.license.exam;

import com.exam.license.exam.models.Authority;
import com.exam.license.exam.models.User;
import com.exam.license.exam.repository.AuthorityRepository;
import com.exam.license.exam.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

//todo globalna obsluga bledow
@SpringBootApplication
public class App {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public App(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public CommandLineRunner initUsers() {
        return args -> {
            Authority adminAuthority = authorityRepository.findByAuthority("ROLE_ADMIN")
                    .orElseGet(() -> authorityRepository.save(new Authority("ROLE_ADMIN")));

            authorityRepository.findByAuthority("ROLE_USER")
                    .orElseGet(() -> authorityRepository.save(new Authority("ROLE_USER")));

            if (userRepository.findByUsername("admin").isEmpty()) {
                User adminUser = new User(
                        "admin",
                        "admin@example.com",
                        passwordEncoder.encode("adminpassword"),
                        Set.of(adminAuthority)
                );
                userRepository.save(adminUser);
            }
        };
    }
}
