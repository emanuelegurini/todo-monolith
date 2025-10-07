package com.todomonolith.todobe.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * STEP 6A: Login Request DTO
 *
 * This DTO (Data Transfer Object) represents the login credentials sent by the client.
 * When a user tries to login, they send this object in the request body.
 *
 * WHY USE A SEPARATE DTO?
 * - Separation of concerns: API layer separate from database entities
 * - Security: Don't expose internal User entity structure
 * - Flexibility: Can change User entity without affecting API
 * - Validation: Can add validation annotations here
 *
 * EXAMPLE REQUEST BODY:
 * {
 *   "email": "user@example.com",
 *   "password": "myPassword123"
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * The user's email address.
     * We use email as the username for authentication.
     */
    private String email;

    /**
     * The user's password in plain text.
     * This will be compared with the BCrypt hashed password from the database.
     * SECURITY NOTE: Always use HTTPS in production to encrypt this data in transit!
     */
    private String password;
}
