package com.openfashion.openfasion_marketplace.services;

import com.openfashion.openfasion_marketplace.models.entities.Role;
import com.openfashion.openfasion_marketplace.models.entities.User;
import com.openfashion.openfasion_marketplace.models.entities.UserRole;
import com.openfashion.openfasion_marketplace.repositories.RoleRepository;
import com.openfashion.openfasion_marketplace.repositories.UserRepository;
import com.openfashion.openfasion_marketplace.repositories.UserRoleRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthenticationService(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public User register(User user) {

        user.setPassword(encoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        UserRole userRole = new UserRole(savedUser, defaultRole);

        userRoleRepository.save(userRole);

        return savedUser;
    }

    public String login(User user) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword()
        ));

        if(authentication.isAuthenticated()) {

            Optional<User> userOptional = userRepository.findByUsername(user.getUsername());

            return jwtService.generateToken(String.valueOf(userOptional.get().getId()));
        } else {
            return "fail";
        }
    }

    public void logout(String token) {
        jwtService.revokeToken(token);
    }

}
