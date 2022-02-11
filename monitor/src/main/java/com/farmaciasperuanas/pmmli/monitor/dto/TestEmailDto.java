package com.farmaciasperuanas.pmmli.monitor.dto;

import lombok.Data;

@Data
public class TestEmailDto {
    private String to;
    private String subject;
    private String body;
}
