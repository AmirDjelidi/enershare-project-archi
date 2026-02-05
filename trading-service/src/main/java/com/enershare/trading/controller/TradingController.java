package com.enershare.trading.controller;

import com.enershare.trading.domain.Bid;
import com.enershare.trading.domain.Offer;
import com.enershare.trading.domain.TradingSession;
import com.enershare.trading.domain.SessionStatus;
import com.enershare.trading.repository.BidRepository;
import com.enershare.trading.repository.TradingSessionRepository;
import com.enershare.trading.service.TradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tradingSessions")
@RequiredArgsConstructor
public class TradingController {

    private final TradingService tradingService;
    private final TradingSessionRepository tradingSessionRepository;
    private final BidRepository bidRepository;
    @GetMapping("/active")
    public ResponseEntity<List<TradingSession>> getActiveOffers() {
        return ResponseEntity.ok(tradingSessionRepository.findByStatus(SessionStatus.ACTIVE));
    }

    @GetMapping("/api/bids/history")
    public ResponseEntity<List<Bid>> getBuyerHistory() {
        return ResponseEntity.ok(bidRepository.findAll());
    }

    @PostMapping("/")
    public ResponseEntity<TradingSession> createTradingSession() {
        TradingSession tradingSession = TradingSession.builder()
                .status(SessionStatus.ACTIVE)
                .build();
        return ResponseEntity.ok(tradingSessionRepository.save(tradingSession));
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<TradingSession> closeTradingSession(
            @RequestParam Long sessionId){
        TradingSession tradingSession = tradingSessionRepository.findById(sessionId)
                        .orElseThrow(() -> new RuntimeException("Session not found"));
        tradingSession.setStatus(SessionStatus.CLOSED);
        return ResponseEntity.ok(tradingSessionRepository.save(tradingSession));
    }

    @PostMapping("/{id}/offer")
    public ResponseEntity<TradingSession> createOffer (
            @RequestParam Long sessionId,
            @RequestParam Long sellerId,
            @RequestParam String name,
            @RequestParam Double energyAmount,
            @RequestParam Double pricePerKwh
            )
    {
        TradingSession session = tradingSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        if (session.getStatus() == SessionStatus.CLOSED){
            throw new RuntimeException("The session is closed");
        }
        Offer createdOffer = Offer
                .builder()
                .sellerId(sellerId)
                .tradingSession(session)
                .name(name)
                .energyAmount(energyAmount)
                .pricePerKwh(pricePerKwh)
                .build();
        List<Offer> offers = session.getOffers();
        offers.add(createdOffer);
        session.setOffers(offers);
        return ResponseEntity.ok(tradingSessionRepository.save(session));
    }

    @PostMapping("/{id}/bid")
    public ResponseEntity<Bid> placeBid(
            @RequestParam Long sessionId,
            @RequestParam Long offerId,
            @RequestParam Long buyerId) {

        Bid createdBid = tradingService.placeBid(sessionId,offerId, buyerId);
        return ResponseEntity.ok(createdBid);
    }
}