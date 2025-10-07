package com.todomonolith.todobe.config;

import com.todomonolith.todobe.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * STEP 9: Security Configuration (Updated for JWT)
 *
 * This class configures Spring Security for JWT-based authentication.
 * It's the central place where we define security rules for our application.
 *
 * KEY CONCEPTS:
 * - SecurityFilterChain: Defines which requests need authentication
 * - AuthenticationManager: Handles the authentication process
 * - PasswordEncoder: Encrypts passwords using BCrypt
 * - JWT Filter: Validates JWT tokens on each request
 *
 * SECURITY FLOW:
 * 1. Request comes in
 * 2. JwtAuthenticationFilter checks for valid JWT token
 * 3. If token valid, user is authenticated
 * 4. SecurityFilterChain checks if user can access the endpoint
 * 5. If authorized, request proceeds to controller
 */
@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    // Our custom JWT filter that we created in STEP 7
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Password Encoder Bean
     *
     * BCrypt is a password hashing function designed for security.
     * WHY BCRYPT?
     * - Slow by design (prevents brute force attacks)
     * - Includes salt (prevents rainbow table attacks)
     * - Adaptive (can increase rounds as computers get faster)
     *
     * USAGE:
     * - Encoding: passwordEncoder.encode("plainPassword") -> "$2a$10$..."
     * - Matching: passwordEncoder.matches("plainPassword", "$2a$10$...") -> true/false
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication Manager Bean
     *
     * This is required for the login endpoint in AuthController.
     * It handles the authentication process when user logs in.
     *
     * HOW IT WORKS:
     * 1. AuthController calls authenticationManager.authenticate()
     * 2. It uses UserService.loadUserByUsername() to get user
     * 3. It uses PasswordEncoder to compare passwords
     * 4. Returns authenticated object or throws exception
     *
     * @param authConfig Spring's authentication configuration
     * @return The authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Security Filter Chain
     *
     * This defines the security rules for our application.
     * It configures which endpoints are public and which require authentication.
     *
     * CONFIGURATION EXPLAINED:
     * 1. Session Management: STATELESS (no sessions, JWT only)
     * 2. CSRF: Disabled (not needed for stateless JWT)
     * 3. Authorization Rules: Which endpoints are public/protected
     * 4. JWT Filter: Added before Spring's authentication filter
     *
     * @param http The HttpSecurity object to configure
     * @return The configured security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // STEP 9.1: Session Management - STATELESS
                // We don't use HTTP sessions because JWT is stateless
                // Each request must include the JWT token
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // STEP 9.2: Disable CSRF Protection
                // CSRF protection is not needed for stateless JWT authentication
                // CSRF attacks target session-based authentication
                .csrf(AbstractHttpConfigurer::disable)

                // STEP 9.3: Configure Authorization Rules
                // Define which endpoints are public and which require authentication
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - anyone can access without token
                        .requestMatchers("/info/**").permitAll()                    // Info endpoints
                        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()  // User registration
                        .requestMatchers("/api/v1/auth/**").permitAll()             // Login endpoint
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Swagger docs

                        // All other endpoints require authentication
                        // User must send valid JWT token in Authorization header
                        .anyRequest().authenticated()
                )

                // STEP 9.4: Add JWT Filter to the filter chain
                // This filter runs BEFORE Spring's UsernamePasswordAuthenticationFilter
                // It checks for JWT token and authenticates the user if token is valid
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
