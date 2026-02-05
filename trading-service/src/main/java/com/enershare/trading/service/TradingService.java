package com.enershare.trading.service;

import com.enershare.trading.domain.*;
import com.enershare.trading.repository.BidRepository;
import com.enershare.trading.repository.TradingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class TradingService {

    private final TradingSessionRepository tradingSessionRepository;
    private final BidRepository bidRepository;
    private final RestTemplate restTemplate;
    @Transactional
    public Bid placeBid(Long sessionId, Long offerId, Long buyerId) {
        TradingSession session = tradingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new RuntimeException("The session is closed");
        }

        Offer offerToBuy = session.getOffers().stream()
                .filter(o -> o.getId().equals(offerId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Offer not found in this session"));
        Double amount = offerToBuy.getPricePerKwh() * offerToBuy.getEnergyAmount();
        // 3.
        restTemplate.postForEntity("http://localhost:8082/api/wallet/" + buyerId + "/withdraw?amount=" + amount, null, Void.class);
        Long sellerId = offerToBuy.getSellerId();
        String creditUrl = "http://localhost:8082/api/wallet/" + sellerId + "/addFunds?amount=" + amount;
        try {
            restTemplate.postForEntity(creditUrl, null, Void.class);
        } catch (Exception e) {
            restTemplate.postForEntity("http://localhost:8082/api/wallet/" + buyerId + "/addFunds?amount=" + amount, null, Void.class);
            throw new RuntimeException("Error during seller payment: " + e.getMessage());
        }
        // 4. Creation of the bid
        Bid bid = Bid.builder()
                .offerId(offerId)
                .buyerId(buyerId)
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .status(BidStatus.ACCEPTED)
                .build();
        // 5. Remove of the offer in the trading session
        session.getOffers().remove(offerToBuy);
        if (session.getOffers().isEmpty()) {
            session.setStatus(SessionStatus.CLOSED);
            System.out.println("Session " + sessionId + " closed : no more offers available.");
        }
        tradingSessionRepository.save(session);
        offerToBuy.setTradingSession(null);
        return bidRepository.save(bid);
    }
}