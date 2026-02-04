package com.enershare.trading.domain;

import jakarta.persistence.*; // BIEN UTILISER JAKARTA
import lombok.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    private Long tradingSessionId;

    private Double energyAmount;
    private Double pricePerKwh;

    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    // Optionnel : Pour la concurrence (Use Case technique)
    @Version
    private Integer version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return Objects.equals(id, offer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}