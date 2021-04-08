package com.sp.auth.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * @author sarvesh
 * @version 0.0.1
 * @since 0.0.1
 */
@Entity
@Table(name = "sp_org")
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", columnDefinition = "SERIAL")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false,
        columnDefinition = "ENUM('CREATED', 'UPDATED', 'DELETED', 'RECREATED', 'DEACTIVATED', 'REACTIVATED')")
    private Status status;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_mod_t", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false)
    private Date lastModifiedTime;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", columnDefinition = "TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP", nullable = false,
        updatable = false)
    private Date createdAt;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "alias")
    private String alias;

    @Column(name = "phone")
    private String phone;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "fax")
    private String fax;

    @Column(name = "preferred_comm")
    private String preferredComm;

    @Column(name = "website")
    private String website;

    @OneToMany(mappedBy = "organization", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Tenant> tenants = new ArrayList<>();

}
