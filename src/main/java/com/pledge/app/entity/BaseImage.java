package com.pledge.app.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public class BaseImage {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String url;
    private String path;
}
