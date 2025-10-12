package com.openfashion.openfasion_marketplace.config;

import com.openfashion.openfasion_marketplace.services.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2JwtSuccessHandler oAuth2JwtSuccessHandler;

    public SecurityConfig(JwtFilter jwtFilter, CustomOAuth2UserService customOAuth2UserService, OAuth2JwtSuccessHandler oAuth2JwtSuccessHandler) {
        this.jwtFilter = jwtFilter;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2JwtSuccessHandler = oAuth2JwtSuccessHandler;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        /* Due to being a stateless app, userInfoEndpoint is rather instabile thats why we moved our verification logic
           oAuth2JwtSuccessHandler
        * */

        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request

                        .requestMatchers("/api/v1/auth/login", "/api/v1/auth/register").anonymous()

                        .requestMatchers(
                                "/api/v1/products/all",
                                "/api/v1/color/all",
                                "/api/v1/size/all",
                                "/oauth2/**",
                                "/login/oauth2/code/**",
                                "/api/v1/auth/oauth2/success"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/products/{id}").permitAll()


                        .requestMatchers(
                                "/api/v1/category/**",
                                "/api/v1/color/**",
                                "/api/v1/size/**",
                                "/api/v1/products/add",
                                "/api/v1/products/delete"

                        ).hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/v1/cart/**", "/api/v1/order/**").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
                       .successHandler(oAuth2JwtSuccessHandler))
                //.httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(10));

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
