package com.enershare.trading.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*; 
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

    @ManyToOne
    @JoinColumn(name = "trading_session_id")
    @JsonIgnore
    private TradingSession tradingSession;

    @Column(nullable = false)
    private Long sellerId;
    private String name;
    private Double energyAmount;
    private Double pricePerKwh;


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