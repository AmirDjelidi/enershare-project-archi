package com.enershare.household.service;

import com.enershare.household.domain.Household;
import com.enershare.household.dto.HouseholdReq;
import com.enershare.household.repository.HouseholdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseholdService {

    private final HouseholdRepository repository;
    private final RestTemplate restTemplate;

    @Transactional
    public Household createHousehold(HouseholdReq req) {
        Household household = Household.builder()
                .ownerName(req.getOwnerName())
                .address(req.getAddress())
                .build();
        Household savedHousehold = repository.save(household);

        try {
            String walletUrl = "http://localhost:8082/api/wallet/" + savedHousehold.getId() + "/newWallet";
            restTemplate.postForEntity(walletUrl, null, Void.class);
            System.out.println("Wallet successfully created for the user " + savedHousehold.getId());
        } catch (Exception e) {
            System.err.println("Wallet creation error: " + e.getMessage());
        }

        return savedHousehold;
    }

    public Household getHousehold(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Home not found"));
    }

    public List<Household> getAllHouseholds() {
        return repository.findAll();
    }

    @Transactional
    public Household updateEnergy(Long id, Double produced, Double consumed) {
        Household household = getHousehold(id); 
        
        if (produced != null) {
            household.setProducedEnergy(household.getProducedEnergy() + produced);
        }
        if (consumed != null) {
            household.setConsumedEnergy(household.getConsumedEnergy() + consumed);
        }
        
        return repository.save(household);
    }

    @Transactional
    public Household deductEnergy(Long id, Double amount) {
        Household household = getHousehold(id);
        if (household.getProducedEnergy() < amount) {
            throw new RuntimeException("Not enough energy in reserve!");
        }
        household.setProducedEnergy(household.getProducedEnergy() - amount);
        return repository.save(household);
    }

    @Transactional
    public Household addEnergy(Long id, Double amount) {
        Household household = getHousehold(id);
        household.setProducedEnergy(household.getProducedEnergy() + amount);
        return repository.save(household);
    }
}