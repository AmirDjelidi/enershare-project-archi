package com.enershare.trading.repository;
import com.enershare.trading.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingSessionRepository extends JpaRepository<TradingSession, Long> {

    @Query("SELECT o FROM TradingSession o WHERE o.status = :status")
    List<TradingSession> findByStatus(@Param("status") SessionStatus status);
}