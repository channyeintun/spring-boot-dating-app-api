package com.pledge.app.config.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Bucket {
    MY_BUCKET("yourbucket");
    private final String name;
}
