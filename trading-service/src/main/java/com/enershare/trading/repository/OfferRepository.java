package com.enershare.trading.repository;
import com.enershare.trading.domain.*;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    // 1. Trouver toutes les offres actives pour une session donnée (pour l'affichage)
    List<Offer> findByTradingSessionIdAndStatus(Long sessionId, OfferStatus status);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Offer o WHERE o.id = :id")
    Optional<Offer> findByIdWithLock(Long id);
}