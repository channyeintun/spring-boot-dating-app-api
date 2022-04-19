package com.pledge.app.payload;

import lombok.Data;

import java.time.Instant;

@Data
public class MessageNotification {
    String from;
    String to;
    String message;
    String sentAt= Instant.now().toString();
}
