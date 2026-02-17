package com.enershare.household.dto;

import lombok.Data;

@Data
public class EnergyUpdateReq {
    private Double produced; 
    private Double consumed; 
}