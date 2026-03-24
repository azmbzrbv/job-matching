package com.jobmatching.security;


import com.jobmatching.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService){
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for APIs (since we use JWT)
                .authorizeHttpRequests(auth -> auth
                        //public endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/candidates/register").permitAll()
                        .requestMatchers("/api/recruiters/register").permitAll()

                        //role-based enpoints
                        .requestMatchers("/api/jobs/create").hasRole("RECRUITER")
                        .requestMatchers("/api/applications/my-apps").hasRole("CANDIDATE")

                        //anything else requires login
                        .anyRequest().authenticated()
                )
                // Tell Spring to use your UserService and PasswordEncoder
                .userDetailsService(userService)
                .httpBasic(Customizer.withDefaults()); // Temporary for testing

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
