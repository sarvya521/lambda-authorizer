
package com.sp.auth.dto;

import com.sp.auth.entity.FeatureAction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
@EqualsAndHashCode(of="uuid")
@Data
@NoArgsConstructor
public class FeatureDto {
    private UUID uuid;

    private String name;

    private List<FeatureAction> featureActions = new ArrayList<>();

    public FeatureDto(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }
}
