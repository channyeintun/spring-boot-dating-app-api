package com.pledge.app.endpoint;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.pledge.app.exception.CustomErrorException;
import com.pledge.app.payload.ApiResponse;
import com.pledge.app.payload.Message;
import com.pledge.app.payload.MessageNotification;
import com.pledge.app.service.FirebaseMessagingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatEndPoint {

    @Autowired
    FirebaseMessagingService firebaseMessagingService;

    @Autowired
    ModelMapper mapper;

    @PostMapping
    public ResponseEntity sendMessage(@RequestBody Message message){
        Map<String, Object> payload = new HashMap<>();
        payload.put("title", "New Message");
        payload.put("body", "From "+message.getSenderName());
        payload.put("data", mapper.map(message, MessageNotification.class));
        try {
            firebaseMessagingService.sendNotificationToToken(payload, message.getFcmToken());
        } catch (FirebaseMessagingException e) {
            throw new CustomErrorException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return ResponseEntity.ok(new ApiResponse(true, "Message sent."));
    }
}
