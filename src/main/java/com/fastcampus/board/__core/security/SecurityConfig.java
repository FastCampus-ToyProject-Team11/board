package com.fastcampus.board.__core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                .headers().frameOptions().sameOrigin()

                .and()
                    .authorizeRequests()
                    .antMatchers("/join", "/auth/**", "/js/**", "/css/**", "/image/**", "/h2-console/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated()

                .and()
                    .formLogin()
                    .loginPage("/auth/loginForm")
                    .loginProcessingUrl("/auth/loginProc")
                    .defaultSuccessUrl("/");

        return http.build();
    }
}
