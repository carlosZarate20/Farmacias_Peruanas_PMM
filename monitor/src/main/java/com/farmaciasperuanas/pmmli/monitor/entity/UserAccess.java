package com.farmaciasperuanas.pmmli.monitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "USER_ACCESS", schema = "SWLI")
public class UserAccess implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USER_ACCESS")
    private Long idUserAccess;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    @JsonIgnore
    private String password;
    @Column(name = "NAME")
    private String name;
    @Column(name = "EMAIL")
    private String email;
    //0 : Activo, 1 : Inactivo, 2 : Bloqueado
    @Column(name = "STATE")
    private Long state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROFILE_USER_ID", referencedColumnName = "ID_PROFILE_USER")
    private ProfileUser profileUser;
}
