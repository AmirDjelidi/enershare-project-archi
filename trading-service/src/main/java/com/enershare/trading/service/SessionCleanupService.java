package com.enershare.trading.service;


import com.enershare.trading.domain.SessionStatus;
import com.enershare.trading.domain.TradingSession;
import com.enershare.trading.repository.TradingSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SessionCleanupService {
    private final TradingSessionRepository tradingSessionRepository;
    @Transactional
    @Scheduled(fixedRate = 600000) 
    public void closeExpiredSession(){
        LocalDateTime threshold = LocalDateTime.now();
        List<TradingSession> sessions = tradingSessionRepository.findByStatus(SessionStatus.ACTIVE);
        if (sessions.isEmpty()){
            System.out.println("No session for now");
            return;
        }
        System.out.println("test");
        for (TradingSession session : sessions){
            LocalDateTime createdAt = session.getCreatedAt();
            System.out.println(createdAt);
            if (createdAt.isBefore(threshold)){
                session.setStatus(SessionStatus.CLOSED);
                System.out.println("Session closed automatically : ID " + session.getId());
            }
        }
        tradingSessionRepository.saveAll(sessions);
    }
}
