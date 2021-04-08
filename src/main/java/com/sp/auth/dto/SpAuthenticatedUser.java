package com.sp.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpAuthenticatedUser {
    private Long id;

    private UUID uuid;

    private String username;

    private List<AuthRoleDto> roles;
}
