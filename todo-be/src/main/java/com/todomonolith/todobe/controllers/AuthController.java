package com.todomonolith.todobe.controllers;

import com.todomonolith.todobe.dto.LoginRequest;
import com.todomonolith.todobe.dto.LoginResponse;
import com.todomonolith.todobe.dto.UserDTO;
import com.todomonolith.todobe.mappers.UserMapper;
import com.todomonolith.todobe.security.JwtUtil;
import com.todomonolith.todobe.security.UserDetailsImpl;
import com.todomonolith.todobe.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    // User service to load user details
    private final UserService userService;

    // Mapper to convert User entity to UserDTO
    private final UserMapper userMapper;

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

    /**
     * STEP 8.10: Validate Token Endpoint
     *
     * This endpoint validates a JWT token and returns the user's data if the token is valid.
     * This is useful for:
     * - Checking if a stored token is still valid when the app starts
     * - Refreshing user data without requiring a new login
     * - Verifying authentication status before making protected requests
     *
     * REQUEST:
     * GET /api/v1/auth/validate-token
     * Headers:
     *   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
     *
     * RESPONSE (Success - 200 OK):
     * {
     *   "valid": true,
     *   "user": {
     *     "id": 1,
     *     "name": "John",
     *     "surname": "Doe",
     *     "email": "user@example.com"
     *   }
     * }
     *
     * RESPONSE (Failure - 401 Unauthorized):
     * {
     *   "valid": false,
     *   "message": "Token is invalid or expired"
     * }
     *
     * @param authorizationHeader The Authorization header containing the JWT token
     * @return ResponseEntity with validation result and user data
     */
    @GetMapping("/validate-token")
    @Operation(summary = "Validate JWT token", description = "Validates a JWT token and returns user data if valid")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        try {
            // STEP 8.11: Extract the token from the Authorization header
            // Header format: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("valid", false);
                errorResponse.put("message", "Missing or invalid Authorization header");
                return ResponseEntity.status(401).body(errorResponse);
            }

            // Remove "Bearer " prefix to get just the token
            String token = authorizationHeader.substring(7);

            // STEP 8.12: Extract username from token
            // This will throw an exception if the token is malformed or has invalid signature
            String username = jwtUtil.extractUsername(token);

            // STEP 8.13: Load user details from database
            UserDetails userDetails = userService.loadUserByUsername(username);

            // STEP 8.14: Validate the token
            // This checks if the token is valid for this user and not expired
            if (jwtUtil.validateToken(token, userDetails)) {
                // Token is valid - get user data and return it
                UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetails;
                com.todomonolith.todobe.entities.User user = userDetailsImpl.getUser();

                // Convert User entity to UserDTO (without password)
                UserDTO userDTO = userMapper.toDTO(user);

                // Build success response
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("user", userDTO);

                return ResponseEntity.ok(response);
            } else {
                // Token is invalid or expired
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("valid", false);
                errorResponse.put("message", "Token is invalid or expired");
                return ResponseEntity.status(401).body(errorResponse);
            }

        } catch (Exception e) {
            // STEP 8.15: Handle any errors during validation
            // This catches malformed tokens, expired tokens, or database errors
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("valid", false);
            errorResponse.put("message", "Token validation failed: " + e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
}
