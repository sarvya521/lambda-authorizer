package com.sp.auth.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
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
@Table(name = "module_features",
    uniqueConstraints =
    @UniqueConstraint(columnNames = {"fk_module_id", "name"}))
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
public class ModuleFeatures {
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

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_module_id", nullable = false)
    private Module module;

    @OneToMany(mappedBy = "moduleFeatures", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ModuleFeaturesClaim> moduleFeaturesClaims = new ArrayList<>();

    @OneToMany(mappedBy = "moduleFeatures", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<RoleFeatures> roleFeatures = new ArrayList<>();
}
