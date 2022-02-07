package com.farmaciasperuanas.pmmli.localstore.entity;

import lombok.Data;
import org.springframework.context.annotation.Profile;

import javax.persistence.*;
import java.awt.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "PROFILE_USER", schema = "SWLI")
public class ProfileUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROFILE_USER")
    private Long idProfileUser;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATE")
    private Long state;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "profileUser")
    private List<UserAccess>  userAccesses;
}