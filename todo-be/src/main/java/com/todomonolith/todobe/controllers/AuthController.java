package com.todomonolith.todobe.controllers;

import com.todomonolith.todobe.dto.LoginRequest;
import com.todomonolith.todobe.dto.LoginResponse;
import com.todomonolith.todobe.security.JwtUtil;
import com.todomonolith.todobe.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * STEP 8: Authentication Controller
 *
 * This controller handles authentication-related endpoints.
 * Currently, it provides a login endpoint that returns a JWT token.
 *
 * WHY SEPARATE AUTH CONTROLLER?
 * - Separation of concerns: Auth logic separate from user management
 * - Clear API structure: /api/v1/auth for authentication endpoints
 * - Easier to add more auth features (logout, refresh token, password reset, etc.)
 *
 * AUTHENTICATION FLOW:
 * 1. Client sends POST to /api/v1/auth/login with email and password
 * 2. AuthenticationManager validates credentials against database
 * 3. If valid, generate JWT token
 * 4. Return token to client
 * 5. Client uses token for subsequent requests
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    // Spring Security's authentication manager - handles the actual authentication
    private final AuthenticationManager authenticationManager;

    // Our JWT utility for token generation
    private final JwtUtil jwtUtil;

    /**
     * STEP 8.1: Login Endpoint
     *
     * This endpoint authenticates a user and returns a JWT token.
     *
     * REQUEST:
     * POST /api/v1/auth/login
     * {
     *   "email": "user@example.com",
     *   "password": "password123"
     * }
     *
     * RESPONSE (Success - 200 OK):
     * {
     *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     *   "type": "Bearer",
     *   "email": "user@example.com",
     *   "name": "John Doe"
     * }
     *
     * RESPONSE (Failure - 401 Unauthorized):
     * {
     *   "error": "Invalid email or password"
     * }
     *
     * @param loginRequest Contains email and password
     * @return ResponseEntity with JWT token or error
     */
    @PostMapping("/login")
    @Operation(summary = "Login user", description = "Authenticate user and return JWT token")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // STEP 8.2: Create an authentication token with email and password
            // This is NOT a JWT token - it's Spring Security's authentication object
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    );

            // STEP 8.3: Authenticate the user
            // AuthenticationManager will:
            // 1. Call UserService.loadUserByUsername() to get user from database
            // 2. Compare submitted password with stored password using BCrypt
            // 3. If match, return authenticated Authentication object
            // 4. If not match, throw BadCredentialsException
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // STEP 8.4: Get the authenticated user's details
            // The Authentication object now contains the authenticated UserDetails
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // STEP 8.5: Generate JWT token for the authenticated user
            String jwt = jwtUtil.generateToken(userDetails);

            // STEP 8.6: Get additional user information to include in response
            // We cast to UserDetailsImpl to access the wrapped User entity
            UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
            String fullName = userDetailsImpl.getUser().getName() + " " +
                            userDetailsImpl.getUser().getSurname();

            // STEP 8.7: Build and return the response with token and user info
            LoginResponse response = LoginResponse.builder()
                    .token(jwt)
                    .type("Bearer")  // Token type - tells client to use "Bearer" in Authorization header
                    .email(userDetails.getUsername())
                    .name(fullName)
                    .build();

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            // STEP 8.8: Handle authentication failure
            // This happens when email doesn't exist or password is wrong
            // Return 401 Unauthorized with error message
            return ResponseEntity
                    .status(401)
                    .body("Invalid email or password");
        } catch (Exception e) {
            // STEP 8.9: Handle any other unexpected errors
            // Return 500 Internal Server Error
            return ResponseEntity
                    .status(500)
                    .body("An error occurred during authentication: " + e.getMessage());
        }
    }
}
