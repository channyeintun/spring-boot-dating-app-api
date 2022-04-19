package com.pledge.app.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pledge.app.service.FirebaseMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseMessagingServiceImpl implements FirebaseMessagingService {

    @Autowired
    private FirebaseMessaging firebaseMessaging;

    @Override
    public String sendNotificationToToken(Map<String, Object> payload, String token) throws FirebaseMessagingException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(payload.get("data"));
        Map<String, String> data = new HashMap<>();
        data.put("data", jsonOutput);
        Notification notification = Notification
                .builder()
                .setTitle((String) payload.get("title"))
                .setBody((String) payload.get("body"))
                .build();
        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .putAllData(data)
                .build();
        return firebaseMessaging.send(message);
    }

    @Override
    public String sendNotificationToTopic(Map<String, Object> payload, String topic) throws FirebaseMessagingException {
        Notification notification = Notification
                .builder()
                .setTitle((String) payload.get("title"))
                .setBody((String) payload.get("body"))
                .build();
        Message message = Message
                .builder()
                .setTopic(topic)
                .setNotification(notification)
                .build();
        return firebaseMessaging.send(message);
    }
}
