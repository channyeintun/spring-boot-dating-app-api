package com.pledge.app.config.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {
    USER_IMAGE("yourbucket");
    private final String bucketName;
}
