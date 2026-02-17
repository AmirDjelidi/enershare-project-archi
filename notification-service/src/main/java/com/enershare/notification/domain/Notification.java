package com.enershare.notification.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    private Long userId; 
    
    private String message;

    @Builder.Default
    private boolean isRead = false; 

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}