package com.enershare.trading.controller;

import com.enershare.trading.domain.Bid;
import com.enershare.trading.domain.Offer;
import com.enershare.trading.domain.OfferStatus;
import com.enershare.trading.repository.OfferRepository;
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
    private final OfferRepository offerRepository;

    // 1. Lister les offres ouvertes pour une session (Use Case 2.3)
    @GetMapping("/{id}/offers")
    public ResponseEntity<List<Offer>> getOpenOffers(@PathVariable("id") Long sessionId) {
        return ResponseEntity.ok(offerRepository.findByTradingSessionIdAndStatus(sessionId, OfferStatus.OPEN));
    }

    // 2. Soumettre un achat / une enchère (Use Case 2.3 & 3 - Concurrence)
    // URL: POST /tradingSessions/1/bid?buyerId=10&amount=15.0
    @PostMapping("/{id}/bid")
    public ResponseEntity<Bid> placeBid(
            @PathVariable("id") Long sessionId,
            @RequestParam Long offerId,
            @RequestParam Long buyerId,
            @RequestParam Double amount) {

        Bid createdBid = tradingService.placeBid(offerId, buyerId, amount);
        return ResponseEntity.ok(createdBid);
    }

    // URL: POST /tradingSessions/1/offer
    @PostMapping("/{id}/offer")
    public ResponseEntity<Offer> createOffer(
            @PathVariable("id") Long sessionId,
            @RequestParam Long sellerId,
            @RequestParam Double energyAmount,
            @RequestParam Double pricePerKwh) {

        Offer offer = Offer.builder()
                .tradingSessionId(sessionId)
                .sellerId(sellerId)
                .energyAmount(energyAmount)
                .pricePerKwh(pricePerKwh)
                .status(OfferStatus.OPEN)
                .build();

        return ResponseEntity.ok(offerRepository.save(offer));
    }
}