package com.pledge.app.payload;

import lombok.Data;

@Data
public class Message {
    String senderName;
    String from;
    String to;
    String message;
    String fcmToken;
}
