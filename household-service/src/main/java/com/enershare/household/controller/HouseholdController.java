package com.enershare.household.controller;

import com.enershare.household.domain.Household;
import com.enershare.household.dto.EnergyUpdateReq;
import com.enershare.household.dto.HouseholdReq;
import com.enershare.household.service.HouseholdService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/households")
@RequiredArgsConstructor
public class HouseholdController {

    private final HouseholdService householdService;

    @PostMapping
    public ResponseEntity<Household> createHousehold(@RequestBody HouseholdReq req) {
        return ResponseEntity.ok(householdService.createHousehold(req));
    }

    @PostMapping("/{id}/meter")
    public ResponseEntity<Household> updateMeter(
            @PathVariable Long id, 
            @RequestBody EnergyUpdateReq req) {
        Household updated = householdService.updateEnergy(id, req.getProduced(), req.getConsumed());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Household> getHouseholdById(@PathVariable Long id) {
        return ResponseEntity.ok(householdService.getHousehold(id));
    }

    @GetMapping
    public ResponseEntity<List<Household>> getAll() {
        return ResponseEntity.ok(householdService.getAllHouseholds());
    }

    @PutMapping("/{id}/deductEnergy")
    public ResponseEntity<Household> deductEnergy(@PathVariable Long id, @RequestParam Double amount) {
        return ResponseEntity.ok(householdService.deductEnergy(id, amount));
    }

    @PutMapping("/{id}/addEnergy")
    public ResponseEntity<Household> addEnergy(@PathVariable Long id, @RequestParam Double amount) {
        return ResponseEntity.ok(householdService.addEnergy(id, amount));
    }
    
}