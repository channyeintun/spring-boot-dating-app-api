package com.pledge.app.service;

import com.google.firebase.messaging.FirebaseMessagingException;

import java.util.Map;

public interface FirebaseMessagingService {
    public String sendNotificationToToken(Map<String,Object> payload,String token) throws FirebaseMessagingException;
    public String sendNotificationToTopic(Map<String,Object> payload,String topic) throws FirebaseMessagingException;
}
