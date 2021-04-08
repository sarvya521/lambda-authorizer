package com.sp.auth.util;

import com.sp.auth.dto.AuthRoleDto;
import com.sp.auth.dto.ClaimDto;
import com.sp.auth.dto.FeatureDto;
import com.sp.auth.dto.SpAuthenticatedUser;
import com.sp.auth.entity.Claim;
import com.sp.auth.entity.FeatureAction;
import com.sp.auth.entity.ModuleFeatures;
import com.sp.auth.entity.ModuleFeaturesClaim;
import com.sp.auth.entity.Role;
import com.sp.auth.entity.RoleFeatures;
import com.sp.auth.entity.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
public final class AuthUtil {

    public static SpAuthenticatedUser prepareUserDto(final User user) {
        List<AuthRoleDto> roles =
                user.getUserRoles().stream()
                        .map(userRole -> prepareUserRoleDto(userRole.getRole()))
                        .collect(Collectors.toList());

        return new SpAuthenticatedUser(
                user.getId(),
                UUIDConverter.longToUuid(user.getId()),
                user.getEmail(),
                roles);
    }

    private static AuthRoleDto prepareUserRoleDto(final Role role) {
        AuthRoleDto authRoleDto = new AuthRoleDto();
        authRoleDto.setUuid(UUIDConverter.longToUuid(role.getId()));
        authRoleDto.setName(role.getName());

        final List<RoleFeatures> roleFeatures = role.getRoleFeatures();
        authRoleDto.setClaims(prepareClaimsForFeatures(roleFeatures));
        authRoleDto.setFeatures(prepareFeatureDtos(roleFeatures));

        filterFeatures(role, authRoleDto);

        return authRoleDto;
    }

    private static void filterFeatures(final Role role, final AuthRoleDto authRoleDto) {
        if (Objects.equals("System Admin", authRoleDto.getName())) {
            final Set<ClaimDto> claims = role.getRoleFeatures().stream()
                    .filter(roleFeatures -> !Objects.equals("User",
                            roleFeatures.getModuleFeatures().getName()))
                    .flatMap(roleFeatures ->
                            roleFeatures.getModuleFeatures().getModuleFeaturesClaims().stream()
                                    .filter(authModuleFeaturesClaim ->
                                            Objects.equals(
                                                    roleFeatures.getFeatureAction(),
                                                    authModuleFeaturesClaim.getFeatureAction()
                                            )
                                    )
                    )
                    .map(ModuleFeaturesClaim::getClaim)
                    .map(AuthUtil::prepareUserClaimDto)
                    .collect(Collectors.toSet());

            authRoleDto.setClaims(claims);
        }
    }

    private static ClaimDto prepareUserClaimDto(final Claim claim) {
        ClaimDto claimDto = new ClaimDto();
        claimDto.setUuid(UUIDConverter.longToUuid(claim.getId()));
        claimDto.setResourceName(claim.getResourceName());
        claimDto.setResourceHttpMethod(claim.getResourceHttpMethod());
        claimDto.setResourceEndpoint(claim.getResourceEndpoint());
        return claimDto;
    }

    private static Set<ClaimDto> prepareClaimsForFeatures(final List<RoleFeatures> roleFeaturesList) {
        return
                roleFeaturesList.stream()
                        .flatMap(roleFeatures ->
                                roleFeatures.getModuleFeatures().getModuleFeaturesClaims().stream()
                                        .filter(authModuleFeaturesClaim ->
                                                Objects.equals(
                                                        roleFeatures.getFeatureAction(),
                                                        authModuleFeaturesClaim.getFeatureAction()
                                                )
                                        )
                        )
                        .map(ModuleFeaturesClaim::getClaim)
                        .map(AuthUtil::prepareUserClaimDto)
                        .collect(Collectors.toSet());
    }

    private static Set<FeatureDto> prepareFeatureDtos(final List<RoleFeatures> roleFeaturesList) {
        Map<UUID, FeatureDto> featuresMap = new HashMap<>();
        roleFeaturesList.forEach(roleFeatures -> {
            final ModuleFeatures moduleFeatures = roleFeatures.getModuleFeatures();
            final UUID uuid = UUIDConverter.longToUuid(moduleFeatures.getId());
            FeatureDto featureDto = null;
            if (featuresMap.containsKey(uuid)) {
                featureDto = featuresMap.get(uuid);
            } else {
                final String feature = moduleFeatures.getName();
                featureDto = new FeatureDto(uuid, feature);
                featuresMap.put(uuid, featureDto);
            }
            final FeatureAction featureAction = roleFeatures.getFeatureAction();
            featureDto.getFeatureActions().add(featureAction);
        });
        return new HashSet<>(featuresMap.values());
    }

    private AuthUtil() {
        throw new AssertionError();
    }
}
