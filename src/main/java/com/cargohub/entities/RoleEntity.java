package com.cargohub.entities;

import com.cargohub.entities.extra.Roles;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "roles")
@Data
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Collection<UserEntity> users;
}
