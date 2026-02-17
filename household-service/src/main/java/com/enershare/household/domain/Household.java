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

    @Builder.Default
    private Double producedEnergy = 0.0; 
    
    @Builder.Default
    private Double consumedEnergy = 0.0;
}