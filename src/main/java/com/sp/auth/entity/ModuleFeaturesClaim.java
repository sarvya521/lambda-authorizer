package com.sp.auth.entity;


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
@Table(name = "module_features_claim")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class ModuleFeaturesClaim {
    @EmbeddedId
    private ModuleFeaturesClaimId id;
    @ManyToOne
    @JoinColumn(name = "fk_module_features_id", insertable = false, updatable = false)
    private ModuleFeatures moduleFeatures;
    @ManyToOne
    @JoinColumn(name = "fk_claim_id", insertable = false, updatable = false)
    private Claim claim;
    @Enumerated(EnumType.STRING)
    @Column(name = "feature_action", nullable = false, insertable = false, updatable = false,
        columnDefinition = "ENUM('VIEW', 'CREATE', 'UPDATE', 'DELETE')")
    private FeatureAction featureAction;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_mod_t", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Date lastModifiedTime;

    public ModuleFeaturesClaim(ModuleFeatures moduleFeatures, Claim claim, FeatureAction featureAction) {
        this.id = new ModuleFeaturesClaimId(moduleFeatures.getId(), claim.getId(), featureAction);
        this.moduleFeatures = moduleFeatures;
        this.claim = claim;
        this.featureAction = featureAction;
    }

    public void setModuleFeatures(ModuleFeatures moduleFeatures) {
        this.moduleFeatures = moduleFeatures;
        this.id.setModuleFeaturesId(moduleFeatures.getId());
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
        this.id.setClaimId(claim.getId());
    }

    public void setFeatureAction(FeatureAction featureAction) {
        this.featureAction = featureAction;
        this.id.setFeatureAction(featureAction);
    }

    @Getter
    @Setter
    @EqualsAndHashCode(of = {"moduleFeaturesId", "claimId", "featureAction"})
    @NoArgsConstructor
    @Embeddable
    public static class ModuleFeaturesClaimId implements Serializable {
        @Column(name = "fk_module_features_id")
        protected Long moduleFeaturesId;

        @Column(name = "fk_claim_id")
        protected Long claimId;

        @Enumerated(EnumType.STRING)
        @Column(name = "feature_action", nullable = false,
            columnDefinition = "ENUM('VIEW', 'CREATE', 'UPDATE', 'DELETE')")
        private FeatureAction featureAction;

        public ModuleFeaturesClaimId(Long moduleFeaturesId, Long claimId, FeatureAction featureAction) {
            this.moduleFeaturesId = moduleFeaturesId;
            this.claimId = claimId;
            this.featureAction = featureAction;
        }
    }
}
