package com.openfashion.openfasion_marketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {

        String encodingId = "bcrypt";

        Map<String, PasswordEncoder> encoders = new HashMap<>();

        encoders.put(encodingId, new BCryptPasswordEncoder());

        //Could use {noop} prefix for oauth password and {bcrypt} for traditional passwords but atm we'll encrypt both
        //the same way

        return new DelegatingPasswordEncoder(encodingId, encoders);
    }

}
