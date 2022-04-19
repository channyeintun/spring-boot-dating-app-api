package com.pledge.app.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class ReportDto {
    private Long id;
    private String category;
    private String description;
    private String reporter;
    private String user;
    private boolean checked;
    private Instant createdAt;
    private Instant updatedAt;
}
