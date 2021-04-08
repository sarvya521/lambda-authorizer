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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
@Entity
@Table(name = "sp_tenant_user")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class UserTenant {
    @EmbeddedId
    private UserTenantId id;
    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", insertable = false, updatable = false)
    private User user;
    @MapsId("tenantId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_tenant_id", insertable = false, updatable = false)
    private Tenant tenant;
    @Column(name = "created_by", nullable = false)
    private Long createdBy;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_mod_t", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Date lastModifiedTime;

    public UserTenant(User user,Tenant tenant, Long createdBy) {
        this.id = new UserTenantId(user.getId(), tenant.getId());
        this.user = user;
        this.tenant = tenant;
        this.createdBy = createdBy;
    }

    public UserTenant(User user,Tenant tenant) {
        this.id = new UserTenantId(user.getId(), tenant.getId());
        this.user = user;
        this.tenant = tenant;
    }

    public void setUser(User user) {
        this.user = user;
        if (Objects.nonNull(user)) {
            this.id.setUserId(user.getId());
        }
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
        if (Objects.nonNull(tenant)) {
            this.id.setTenantId(tenant.getId());
        }
    }

    @Getter
    @Setter
    @EqualsAndHashCode(of = {"userId", "tenantId"})
    @NoArgsConstructor
    @Embeddable
    public static class UserTenantId implements Serializable {
        @Column(name = "fk_user_id")
        protected Long userId;

        @Column(name = "fk_tenant_id")
        protected Long tenantId;

        public UserTenantId(Long userId, Long tenantId) {
            this.userId = userId;
            this.tenantId = tenantId;
        }
    }
}
