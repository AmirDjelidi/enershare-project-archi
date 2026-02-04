package com.enershare.trading.service;

import com.enershare.trading.domain.Bid;
import com.enershare.trading.domain.BidStatus;
import com.enershare.trading.domain.Offer;
import com.enershare.trading.domain.OfferStatus;
import com.enershare.trading.repository.BidRepository;
import com.enershare.trading.repository.OfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TradingService {

    private final OfferRepository offerRepository;
    private final BidRepository bidRepository;
    private final RestTemplate restTemplate;
    /**
     * Logique technique pour acheter une offre d'énergie
     */
    @Transactional // CRITIQUE : Ouvre une transaction pour le verrouillage (Lock)
    public Bid placeBid(Long offerId, Long buyerId, Double amount) {

        // 1. On récupère l'offre et on la VERROUILLE en base de données
        // Personne d'autre ne peut modifier cette ligne tant que la méthode n'est pas finie.
        Offer offer = offerRepository.findByIdWithLock(offerId)
                .orElseThrow(() -> new RuntimeException("Offre introuvable"));

        // 2. Vérification métier : L'offre est-elle toujours disponible ?
        if (!offer.getStatus().equals(OfferStatus.OPEN)) {
            throw new RuntimeException("Cette offre n'est plus disponible");
        }

        // 3.
        // Appel au Wallet Service via RestTemplate
        String url = "http://localhost:8082/api/wallets/" + buyerId + "/balance";
        Double balance = restTemplate.getForObject(url, Double.class);
        if (balance == null || balance < amount) {
            throw new RuntimeException("Solde insuffisant !");
        }
        // Débit
        restTemplate.postForEntity("http://localhost:8082/api/wallets/" + buyerId + "/debit?amount=" + amount, null, Void.class);

        Long sellerId = offer.getSellerId();
        String creditUrl = "http://localhost:8082/api/wallets/" + sellerId + "/credit?amount=" + amount;

        try {
            restTemplate.postForEntity(creditUrl, null, Void.class);
        } catch (Exception e) {
            restTemplate.postForEntity("http://localhost:8082/api/wallets/" + buyerId + "/credit?amount=" + amount, null, Void.class);
            throw new RuntimeException("Erreur lors du paiement du vendeur : " + e.getMessage());
        }
        // 4. Création de l'enchère (Bid)
        Bid bid = Bid.builder()
                .offerId(offerId)
                .buyerId(buyerId)
                .amount(amount)
                .timestamp(LocalDateTime.now())
                .status(BidStatus.ACCEPTED)
                .build();

        // 5. Mise à jour du statut de l'offre
        offer.setStatus(OfferStatus.COMPLETED);
        offerRepository.save(offer);

        return bidRepository.save(bid);
    }
}