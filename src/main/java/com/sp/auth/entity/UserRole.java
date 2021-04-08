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
@Table(name = "sp_user_role")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class UserRole {
    @EmbeddedId
    private UserRoleId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", insertable = false, updatable = false)
    private User user;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_role_id", insertable = false, updatable = false)
    private Role role;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_mod_t", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Date lastModifiedTime;

    public UserRole(User user, Role role, Long createdBy) {
        this.id = new UserRoleId(user.getId(), role.getId());
        this.user = user;
        this.role = role;
        this.createdBy = createdBy;
    }

    public UserRole(User user, Role role) {
        this.id = new UserRoleId(user.getId(), role.getId());
        this.user = user;
        this.role = role;
    }

    public void setUser(User user) {
        this.user = user;
        if (Objects.nonNull(user)) {
            this.id.setUserId(user.getId());
        }
    }

    public void setRole(Role role) {
        this.role = role;
        if (Objects.nonNull(role)) {
            this.id.setRoleId(role.getId());
        }
    }

    @Getter
    @Setter
    @EqualsAndHashCode(of = {"userId", "roleId"})
    @NoArgsConstructor
    @Embeddable
    public static class UserRoleId implements Serializable {
        @Column(name = "fk_user_id")
        protected Long userId;

        @Column(name = "fk_role_id")
        protected Long roleId;

        public UserRoleId(Long userId, Long roleId) {
            this.userId = userId;
            this.roleId = roleId;
        }
    }
}
