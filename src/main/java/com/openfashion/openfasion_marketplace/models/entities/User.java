package com.openfashion.openfasion_marketplace.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column
    private String email;
    @Column
    private Boolean enabled;
    @Column(nullable = false)
    private Boolean account_not_expired;
    @Column(nullable = false)
    private Boolean account_not_locked;
    @Column(nullable = false)
    private Boolean credential_not_expired;
    @Column
    private String first_name;
    @Column
    private String last_name;

    // The mappedBy attribute indicates that the user entity is the owner of the relationship
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserSocialAccount> socialAccounts = new HashSet<>();

    // We are mapping the intermediary join table "user_role" explicitly, this allows us to add extra attributes
    // to the join table if needed
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserRole> userRoles = new HashSet<>();

}
