package com.sp.auth.dao;

import com.sp.auth.dto.GhAuthenticatedUser;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
public interface UserDao {
    GhAuthenticatedUser findByEmail(String email);
}
