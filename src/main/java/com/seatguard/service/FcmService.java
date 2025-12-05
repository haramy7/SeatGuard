package com.seatguard.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.seatguard.model.Role;
import com.seatguard.model.User;
import com.seatguard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final UserRepository userRepository;

    public void sendNotificationToAdmins(String title, String body) {
        List<User> admins = userRepository.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMIN && u.getFcmToken() != null)
                .toList();

        if (admins.isEmpty()) {
            System.out.println("⚠️ 알림을 받을 관리자가 없어용 (토큰 등록 안됨)");
            return;
        }

        for (User admin : admins) {
            try {
                Message message = Message.builder()
                        .setToken(admin.getFcmToken())
                        .setNotification(Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .build();

                String response = FirebaseMessaging.getInstance().send(message);
                System.out.println("🚀 Push Sent to " + admin.getName() + ": " + response);
            } catch (Exception e) {
                System.err.println("❌ Push Failed to " + admin.getName());
            }
        }
    }
}
