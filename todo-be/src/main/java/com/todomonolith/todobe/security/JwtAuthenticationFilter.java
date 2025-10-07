package com.todomonolith.todobe.security;

import com.todomonolith.todobe.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * STEP 7: JWT Authentication Filter
 *
 * This filter intercepts EVERY incoming HTTP request and checks for a valid JWT token.
 * It extends OncePerRequestFilter to ensure it's executed only once per request.
 *
 * WHY DO WE NEED THIS FILTER?
 * - JWT is stateless - server doesn't store session data
 * - We need to validate the token on EVERY request
 * - Extract user information from token and set it in Spring Security context
 *
 * REQUEST FLOW WITH THIS FILTER:
 * 1. Client sends request with: Authorization: Bearer <token>
 * 2. This filter intercepts the request
 * 3. Extract token from Authorization header
 * 4. Validate token and extract username
 * 5. Load user from database
 * 6. Set authentication in SecurityContext
 * 7. Continue with the request
 *
 * If token is invalid or missing, the request continues but user won't be authenticated.
 * Protected endpoints will then reject the request with 401/403.
 */
@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * This method is called for every HTTP request.
     * It's the heart of JWT authentication.
     *
     * @param request The HTTP request
     * @param response The HTTP response
     * @param filterChain Chain of filters - used to continue processing the request
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // STEP 7.1: Extract the Authorization header from the request
        // Expected format: "Authorization: Bearer <jwt-token>"
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // STEP 7.2: Check if Authorization header exists and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract the token by removing "Bearer " prefix (7 characters)
            jwt = authorizationHeader.substring(7);

            try {
                // STEP 7.3: Extract username (email) from the token
                // This also validates the token signature
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Token is invalid (expired, malformed, wrong signature, etc.)
                // Log the error and continue without authentication
                logger.error("JWT Token extraction failed: " + e.getMessage());
            }
        }

        // STEP 7.4: If we have a username and user is not already authenticated
        // SecurityContextHolder.getContext().getAuthentication() is null when user is not authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // STEP 7.5: Load user details from database using the username from token
            UserDetails userDetails = userService.loadUserByUsername(username);

            // STEP 7.6: Validate the token
            // Check if token is valid for this user and hasn't expired
            if (jwtUtil.validateToken(jwt, userDetails)) {

                // STEP 7.7: Create authentication token for Spring Security
                // This tells Spring Security that the user is authenticated
                // We pass: userDetails, credentials (null for JWT), and authorities
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,  // No credentials needed - token is the credential
                                userDetails.getAuthorities()
                        );

                // STEP 7.8: Add request details to the authentication object
                // This includes IP address, session ID, etc.
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // STEP 7.9: Set the authentication in Spring Security context
                // From now on, SecurityContextHolder will know the user is authenticated
                // Controllers can access this using @AuthenticationPrincipal or SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // STEP 7.10: Continue with the filter chain
        // Pass the request to the next filter or to the controller
        filterChain.doFilter(request, response);
    }
}
