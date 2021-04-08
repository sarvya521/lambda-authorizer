package com.sp.auth.entity;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
@Entity
@Table(name = "role_features")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class RoleFeatures {
    @EmbeddedId
    private RoleFeaturesId id;
    @MapsId("roleId")
    @ManyToOne
    @JoinColumn(name = "fk_role_id", insertable = false, updatable = false)
    private Role role;
    @ManyToOne
    @JoinColumn(name = "fk_module_features_id", insertable = false, updatable = false)
    private ModuleFeatures moduleFeatures;
    @Enumerated(EnumType.STRING)
    @Column(name = "feature_action", nullable = false, insertable = false, updatable = false,
        columnDefinition = "ENUM('VIEW', 'CREATE', 'UPDATE', 'DELETE')")
    private FeatureAction featureAction;
    @Column(name = "created_by", nullable = false)
    private Long createdBy;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_mod_t", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Date lastModifiedTime;

    public RoleFeatures(Role role, ModuleFeatures moduleFeatures, FeatureAction featureAction, Long createdBy) {
        this.id = new RoleFeaturesId(role.getId(), moduleFeatures.getId(), featureAction);
        this.role = role;
        this.moduleFeatures = moduleFeatures;
        this.featureAction = featureAction;
        this.createdBy = createdBy;
    }

    public void setRole(Role role) {
        this.role = role;
        this.id.setRoleId(role.getId());
    }

    public void setModuleFeatures(ModuleFeatures moduleFeatures) {
        this.moduleFeatures = moduleFeatures;
        this.id.setModuleFeaturesId(moduleFeatures.getId());
    }

    public void setFeatureAction(FeatureAction featureAction) {
        this.featureAction = featureAction;
        this.id.setFeatureAction(featureAction);
    }

    @Getter
    @Setter
    @EqualsAndHashCode(of = {"roleId", "moduleFeaturesId", "featureAction"})
    @NoArgsConstructor
    @Embeddable
    public static class RoleFeaturesId implements Serializable {
        @Column(name = "fk_role_id")
        protected Long roleId;

        @Column(name = "fk_module_features_id")
        protected Long moduleFeaturesId;

        @Enumerated(EnumType.STRING)
        @Column(name = "feature_action", nullable = false,
            columnDefinition = "ENUM('VIEW', 'CREATE', 'UPDATE', 'DELETE')")
        private FeatureAction featureAction;

        public RoleFeaturesId(Long roleId, Long moduleFeaturesId, FeatureAction featureAction) {
            this.roleId = roleId;
            this.moduleFeaturesId = moduleFeaturesId;
            this.featureAction = featureAction;
        }
    }
}
