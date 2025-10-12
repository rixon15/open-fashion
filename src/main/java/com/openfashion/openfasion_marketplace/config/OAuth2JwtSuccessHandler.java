package com.openfashion.openfasion_marketplace.config;

import com.openfashion.openfasion_marketplace.models.entities.Role;
import com.openfashion.openfasion_marketplace.models.entities.User;
import com.openfashion.openfasion_marketplace.models.entities.UserRole;
import com.openfashion.openfasion_marketplace.repositories.RoleRepository;
import com.openfashion.openfasion_marketplace.repositories.UserRepository;
import com.openfashion.openfasion_marketplace.repositories.UserRoleRepository;
import com.openfashion.openfasion_marketplace.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.UUID;

@Component
public class OAuth2JwtSuccessHandler implements AuthenticationSuccessHandler {


    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public OAuth2JwtSuccessHandler(JwtService jwtService, UserRepository userRepository,
                                   RoleRepository roleRepository, UserRoleRepository userRoleRepository,
                                   PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        if(email == null){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "oAuth2 login succeeded but the email is missing");
        }


        User localUser = userRepository.findByUsername(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(email);
                    newUser.setEmail(email);
                    newUser.setPassword(passwordEncoder.encode(String.valueOf(UUID.randomUUID())));
                    newUser.setFirst_name(oAuth2User.getAttribute("given_name"));
                    newUser.setLast_name(oAuth2User.getAttribute("family_name"));
                    newUser.setEnabled(true);

                    User savedUser = userRepository.save(newUser);

                    Role defaultRole = roleRepository.findByName("USER")
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                    UserRole userRole = new UserRole(savedUser, defaultRole);
                    userRoleRepository.save(userRole);

                    return savedUser;
                });

        String jwtToken = jwtService.generateToken(String.valueOf(localUser.getId()));

        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Login successful\", \"token\": \"" + jwtToken + "\"}");
        response.setStatus(HttpServletResponse.SC_OK);

        response.getWriter().flush();

    }
}
