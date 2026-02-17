package com.enershare.notification.controller;

import com.enershare.notification.domain.Notification;
import com.enershare.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @PostMapping
    public ResponseEntity<Notification> sendNotification(
            @RequestParam Long userId, 
            @RequestParam String message) {
            
        Notification notif = Notification.builder()
                .userId(userId)
                .message(message)
                .build();
        return ResponseEntity.ok(notificationRepository.save(notif));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(notificationRepository.findByUserIdOrderByCreatedAtDesc(userId));
    }
    
    // 3. (Optionnel)
    @PatchMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable("id") Long id) {
        Notification notif = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notif.setRead(true);
        return ResponseEntity.ok(notificationRepository.save(notif));
    }
}