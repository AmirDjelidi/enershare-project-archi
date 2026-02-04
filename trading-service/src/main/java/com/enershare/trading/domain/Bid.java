package com.enershare.trading.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long buyerId;   // Qui achète (ID du Household)

    private Long offerId;   // Quelle offre est visée

    private Double amount;  // Montant proposé (en €)

    private LocalDateTime timestamp; // Heure de l'enchère

    @Enumerated(EnumType.STRING)
    private BidStatus status;
}
