package com.fastcampus.board.__core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .headers().frameOptions().sameOrigin()

                .and().formLogin().disable() // x-www-form-urlencoded, UsernamePasswordAuthenticationFilter disable

                .httpBasic().disable() // HttpBasicAuthenticationFilter disable

                .authorizeRequests()
                    .antMatchers("/join", "/auth/**", "/js/**", "/css/**", "/image/**", "/h2-console/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()

                .and().apply(new SecurityFilterManager());

        return http.build();
    }

    public class SecurityFilterManager extends AbstractHttpConfigurer<SecurityFilterManager, HttpSecurity> {

        @Override
        public void configure(HttpSecurity builder) throws Exception {
             AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            builder.addFilter(new CustomUsernamePasswordAuthenticationFilter(authenticationManager));
            super.configure(builder);
        }
    }
}
