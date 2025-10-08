package com.todomonolith.todobe.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * STEP 5: JWT Utility Class
 *
 * This class handles all JWT operations: generation, validation, and extraction of information.
 * JWT (JSON Web Token) is a compact, URL-safe token format for securely transmitting information.
 *
 * WHY USE JWT?
 * - Stateless: Server doesn't need to store session data
 * - Scalable: Works across multiple servers
 * - Secure: Digitally signed to prevent tampering
 * - Self-contained: Contains all user information needed
 *
 * JWT STRUCTURE: header.payload.signature
 * - Header: Algorithm and token type
 * - Payload: User data (claims)
 * - Signature: Ensures token hasn't been modified
 */
@Component
public class JwtUtil {

    // Injected from application.yaml - the secret key for signing tokens
    @Value("${jwt.secret}")
    private String secret;

    // Injected from application.yaml - how long token is valid (in milliseconds)
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Extract the username (email) from the JWT token.
     * This is called on every request to identify which user is making the request.
     *
     * @param token The JWT token from the Authorization header
     * @return The username (email) stored in the token
     */
    public String extractUsername(String token) {
        // Extract the "subject" claim, which we use to store the username
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract the expiration date from the token.
     * Used to check if the token is still valid.
     *
     * @param token The JWT token
     * @return The expiration date
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Generic method to extract any claim from the token.
     * Claims are pieces of information stored in the JWT payload.
     *
     * @param token The JWT token
     * @param claimsResolver Function to extract specific claim
     * @return The extracted claim value
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Parse the token and extract all claims (data stored in token).
     * This method also validates the signature using our secret key.
     *
     * @param token The JWT token
     * @return All claims from the token
     */
    private Claims extractAllClaims(String token) {
        // SECURITY: Strict validation - reject tokens with invalid Base64 padding
        validateTokenFormat(token);

        return Jwts.parser()
                .verifyWith(getSigningKey())  // Verify signature with our secret key
                .build()
                .parseSignedClaims(token)     // Parse and validate the token
                .getPayload();                // Get the claims (payload)
    }

    /**
     * Validates the JWT token format to ensure strict Base64 encoding.
     * This prevents accepting tokens with modified padding bits that decode to the same value.
     *
     * SECURITY RATIONALE:
     * Base64 padding bits in the last character can be modified without changing the decoded value.
     * For example, in HS512 signatures, 'fw', 'f1', 'f2', etc. all decode identically.
     * This method ensures only the canonical (properly padded) form is accepted.
     *
     * @param token The JWT token to validate
     * @throws io.jsonwebtoken.MalformedJwtException if token format is invalid
     */
    private void validateTokenFormat(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new io.jsonwebtoken.MalformedJwtException("JWT must have 3 parts");
        }

        // Validate signature part has correct Base64URL encoding with proper padding
        String signature = parts[2];

        // Verify by decoding and re-encoding to get canonical form
        if (signature.length() > 0) {
            try {
                byte[] decoded = java.util.Base64.getUrlDecoder().decode(signature);
                String canonical = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(decoded);

                if (!signature.equals(canonical)) {
                    throw new io.jsonwebtoken.security.SignatureException(
                        "JWT signature has invalid Base64 padding - token may have been tampered with"
                    );
                }
            } catch (IllegalArgumentException e) {
                throw new io.jsonwebtoken.MalformedJwtException("Invalid Base64 encoding in signature");
            }
        }
    }

    /**
     * Check if the token has expired.
     *
     * @param token The JWT token
     * @return true if token is expired, false otherwise
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generate a new JWT token for a user.
     * This is called after successful login.
     *
     * WHAT GETS STORED IN THE TOKEN?
     * - Subject: Username (email)
     * - Issued At: When token was created
     * - Expiration: When token expires
     * - Custom claims: id, name, surname, email
     *
     * @param userDetails The authenticated user's details
     * @return A new JWT token string
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Extract user information from UserDetailsImpl
        if (userDetails instanceof com.todomonolith.todobe.security.UserDetailsImpl) {
            com.todomonolith.todobe.security.UserDetailsImpl userDetailsImpl =
                (com.todomonolith.todobe.security.UserDetailsImpl) userDetails;
            com.todomonolith.todobe.entities.User user = userDetailsImpl.getUser();

            // Add custom claims
            claims.put("id", user.getId());
            claims.put("name", user.getName());
            claims.put("surname", user.getSurname());
            claims.put("email", user.getEmail());
        }

        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Create the actual JWT token with claims and subject.
     *
     * TOKEN CREATION PROCESS:
     * 1. Set custom claims (additional data)
     * 2. Set subject (username/email)
     * 3. Set issued date (now)
     * 4. Set expiration date (now + expiration time)
     * 5. Sign with our secret key using HS256 algorithm
     *
     * @param claims Custom data to include in token
     * @param subject The username (email)
     * @return The complete JWT token string
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims(claims)                          // Add custom claims
                .subject(subject)                        // Set username (email)
                .issuedAt(new Date(System.currentTimeMillis()))  // Token creation time
                .expiration(new Date(System.currentTimeMillis() + expiration))  // Token expiration
                .signWith(getSigningKey())               // Sign with secret key
                .compact();                              // Build and serialize to string
    }

    /**
     * Validate if a token is valid for a specific user.
     *
     * VALIDATION CHECKS:
     * 1. Username in token matches the user
     * 2. Token has not expired
     *
     * @param token The JWT token
     * @param userDetails The user details to validate against
     * @return true if token is valid, false otherwise
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Check if username matches and token is not expired
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Get the signing key used to sign and verify JWT tokens.
     * We use HMAC-SHA256 algorithm (HS256).
     *
     * WHY HMAC-SHA256?
     * - Fast and efficient
     * - Secure for most applications
     * - Widely supported
     *
     * @return The secret key for signing
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
