package com.todomonolith.todobe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * STEP 6B: Login Response DTO
 *
 * This DTO represents the response sent back to the client after successful login.
 * It contains the JWT token that the client will use for subsequent requests.
 *
 * HOW THE CLIENT USES THIS:
 * 1. Client receives this response with the token
 * 2. Client stores the token (localStorage, sessionStorage, or memory)
 * 3. Client includes token in Authorization header for future requests:
 *    Authorization: Bearer <token>
 *
 * EXAMPLE RESPONSE BODY:
 * {
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *   "type": "Bearer",
 *   "email": "user@example.com"
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    /**
     * The JWT token string.
     * This is what the client needs to authenticate future requests.
     */
    private String token;

    /**
     * The token type (always "Bearer" for JWT).
     * This tells the client how to format the Authorization header.
     */
    private String type;

    /**
     * The user's email for confirmation.
     * Helps the client know which user is logged in.
     */
    private String email;

    /**
     * Optional: The user's full name for displaying in the UI.
     */
    private String name;
}
