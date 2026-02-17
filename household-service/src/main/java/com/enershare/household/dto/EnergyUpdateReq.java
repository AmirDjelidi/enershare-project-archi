package com.enershare.household.dto;

import lombok.Data;

@Data
public class EnergyUpdateReq {
    private Double produced; // Énergie produite en kWh (ex: Solaire)
    private Double consumed; // Énergie consommée en kWh
}