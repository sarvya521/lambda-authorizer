
package com.sp.auth.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
@EqualsAndHashCode(of="uuid")
@Data
@NoArgsConstructor
public class AuthRoleDto {
    private UUID uuid;

    private String name;

    private Set<ClaimDto> claims = new LinkedHashSet<>();

    private Set<FeatureDto> features = new LinkedHashSet<>();
}
