package com.cargohub.entities;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillingDetailsEntity> billingDetails;

    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"))
    private Collection<RoleEntity> roles;
}
