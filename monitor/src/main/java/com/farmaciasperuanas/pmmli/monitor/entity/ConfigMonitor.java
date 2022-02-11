package com.farmaciasperuanas.pmmli.monitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "CONFIG_MONITOR", schema = "SWLI")
public class ConfigMonitor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONFIG_MONITOR")
    private Long idConfigMonitor;

    @Column(name = "EMAIL")
    private String email;
}