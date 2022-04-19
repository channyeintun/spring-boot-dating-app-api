package com.pledge.app.payload;

import lombok.Data;

@Data
public class ReportRequest {
    private Long id;
    private String category;
    private String description;
    private String reporter;
    private String user;
}
