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
        // 1. Sauvegarde le foyer en BDD [cite: 25]
        Household household = Household.builder()
                .ownerName(req.getOwnerName())
                .address(req.getAddress())
                .build();
        Household savedHousehold = repository.save(household);

        // 2. Appelle le Wallet-Service pour lui créer un portefeuille [cite: 16, 60]
        try {
            String walletUrl = "http://localhost:8082/api/wallet/" + savedHousehold.getId() + "/newWallet";
            restTemplate.postForEntity(walletUrl, null, Void.class);
            System.out.println("Wallet créé avec succès pour le user " + savedHousehold.getId());
        } catch (Exception e) {
            System.err.println("Erreur de création de Wallet: " + e.getMessage());
        }

        return savedHousehold;
    }

    public Household getHousehold(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foyer non trouvé"));
    }

    public List<Household> getAllHouseholds() {
        return repository.findAll();
    }

    @Transactional
    public Household updateEnergy(Long id, Double produced, Double consumed) {
        Household household = getHousehold(id); // Récupère le foyer (ou lance une erreur s'il n'existe pas)
        
        // Ajoute la nouvelle énergie aux compteurs existants
        if (produced != null) {
            household.setProducedEnergy(household.getProducedEnergy() + produced);
        }
        if (consumed != null) {
            household.setConsumedEnergy(household.getConsumedEnergy() + consumed);
        }
        
        return repository.save(household);
    }
}