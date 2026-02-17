package com.enershare.household.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Household {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false)
    private String address;

    // Smart Meter Data [cite: 15, 26]
    @Builder.Default
    private Double producedEnergy = 0.0; 
    
    @Builder.Default
    private Double consumedEnergy = 0.0;
}