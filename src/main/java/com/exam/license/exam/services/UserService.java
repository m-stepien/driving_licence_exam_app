package com.exam.license.exam.services;

import com.exam.license.exam.exceptions.NoSuchElementInDatabaseException;
import com.exam.license.exam.models.Authority;
import com.exam.license.exam.models.User;
import com.exam.license.exam.repository.AuthorityRepository;
import com.exam.license.exam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Authority authority = authorityRepository.findByAuthority("ROLE_USER")
                .orElseGet(() -> authorityRepository.save(new Authority("ROLE_USER")));
        user.addAuthority(authority);
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username){
        return this.userRepository.findByUsername(username);
    }

    public long findIdByUsername(String username) throws NoSuchElementInDatabaseException{
        return this.findByUsername(username).orElseThrow(NoSuchElementInDatabaseException::new).getId();
    }
}
