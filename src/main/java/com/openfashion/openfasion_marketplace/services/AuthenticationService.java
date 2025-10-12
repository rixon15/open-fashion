package com.openfashion.openfasion_marketplace.services;

import com.openfashion.openfasion_marketplace.models.entities.Role;
import com.openfashion.openfasion_marketplace.models.entities.User;
import com.openfashion.openfasion_marketplace.models.entities.UserRole;
import com.openfashion.openfasion_marketplace.repositories.RoleRepository;
import com.openfashion.openfasion_marketplace.repositories.UserRepository;
import com.openfashion.openfasion_marketplace.repositories.UserRoleRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthenticationService(UserRepository userRepository, UserRoleRepository userRoleRepository,
                                 RoleRepository roleRepository, AuthenticationManager authenticationManager,
                                 JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }


    public User register(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

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

            User userOptional = userRepository.findByUsername(user.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return jwtService.generateToken(String.valueOf(userOptional.getId()));
        } else {
            throw new BadCredentialsException("Authentication failed for user: " + user.getUsername());
        }
    }

    public void logout(String token) {
        jwtService.revokeToken(token);
    }

    public String currentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            return "No user is currently authenticated";
        }

        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails user) {
            return user.getUsername();
        } else {
            return principal.toString();
        }

    }

}
