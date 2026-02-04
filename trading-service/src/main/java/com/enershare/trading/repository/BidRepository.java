package com.enershare.trading.repository;

import com.enershare.trading.domain.Bid;
import com.enershare.trading.domain.Offer;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findByBuyerId(Long buyerId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM Bid o WHERE o.buyerId = :id")
    Optional<Bid> findByIdWithLock(Long id);

    List<Bid> findByOfferId(Long offerId);
}