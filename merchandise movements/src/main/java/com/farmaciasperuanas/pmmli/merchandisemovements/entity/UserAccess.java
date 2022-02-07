package com.farmaciasperuanas.pmmli.merchandisemovements.entity;

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
    private String password;
    @Column(name = "NAME")
    private String name;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "STATE")
    private Long state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROFILE_USER_ID", referencedColumnName = "ID_PROFILE_USER")
    @JsonIgnore
    private ProfileUser profileUser;
}
