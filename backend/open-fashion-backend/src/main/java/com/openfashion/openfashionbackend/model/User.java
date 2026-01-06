package com.openfashion.openfashionbackend.model;

import com.openfashion.openfashionbackend.model.common.AuditableEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "\"user\"")
public class User extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private Boolean enabled = true;

    @Column(nullable = false)
    private Boolean accountNotLocked = true;

    @Column(nullable = false)
    private Boolean credentialsNotExpired = true;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @ToString.Exclude
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<UserAddress> addresses = new ArrayList<>();
}
