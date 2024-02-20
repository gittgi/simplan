package com.gittgi.simplan.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "user")
@Setter
@Getter
@Table(uniqueConstraints = { @UniqueConstraint(name = "username", columnNames = {"username"}) })
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    private String role;

    private String social = null;

    @OneToMany(mappedBy = "user")
    private List<PlanEntity> plans = new ArrayList<>();
}
