package com.todomonolith.todobe.security;

import com.todomonolith.todobe.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * STEP 3: Custom UserDetails Implementation
 *
 * This class wraps our User entity and implements Spring Security's UserDetails interface.
 * Spring Security needs UserDetails to perform authentication and authorization.
 *
 * WHY DO WE NEED THIS?
 * - Spring Security doesn't know about our User entity directly
 * - UserDetails is the contract that Spring Security understands
 * - This adapter/wrapper pattern connects our User entity to Spring Security
 *
 * WHAT DOES UserDetails PROVIDE?
 * - Username (we use email as username)
 * - Password (encrypted password from database)
 * - Authorities/Roles (permissions the user has)
 * - Account status flags (locked, expired, enabled, etc.)
 */
@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

    // The actual user entity from our database
    private final User user;

    /**
     * Returns the authorities (roles/permissions) granted to the user.
     * For this didactic example, we return an empty list.
     * In a real application, you would return roles like ROLE_USER, ROLE_ADMIN, etc.
     *
     * @return Collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO: In a real application, fetch roles from database
        // Example: return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        return Collections.emptyList();
    }

    /**
     * Returns the password used to authenticate the user.
     * This should be the BCrypt encoded password from the database.
     *
     * @return The encrypted password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username used to authenticate the user.
     * We use email as the username in this application.
     *
     * @return The user's email
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indicates whether the user's account has expired.
     * For this example, accounts never expire.
     * In production, you might track account expiration dates.
     *
     * @return true if the account is valid (not expired)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     * For this example, accounts are never locked.
     * In production, you might lock accounts after failed login attempts.
     *
     * @return true if the account is not locked
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     * For this example, credentials never expire.
     * In production, you might require password changes every 90 days.
     *
     * @return true if the credentials are valid (not expired)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     * For this example, all users are enabled.
     * In production, you might have an 'active' flag in the User table.
     *
     * @return true if the user is enabled
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
