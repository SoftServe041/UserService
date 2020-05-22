package com.cargohub.entities;

import com.cargohub.entities.extra.Role;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private Role role;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Collection<UserEntity> users;
}
