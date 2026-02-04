package com.enershare.wallet.controller;

import com.enershare.wallet.domain.Wallet;
import com.enershare.wallet.repository.WalletRepository;
import com.enershare.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final WalletRepository walletRepository;

    // Endpoint pour consulter le solde

    @GetMapping("/{userId}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getBalance(userId));
    }

    @PostMapping("/{userId}/wallet")
    public ResponseEntity<Wallet> createWallet(@PathVariable("userId")Long userId){
        Wallet wallet = Wallet.builder().
                userId(userId).
                balance(0.0).
                build();
        return ResponseEntity.ok(walletRepository.save(wallet));
    }

    // Endpoint pour débiter (utilisé par le Trading lors d'un achat)
    // URL: POST http://localhost:8082/api/wallets/{userId}/debit?amount=10.0
    @PostMapping("/{userId}/debit")
    public ResponseEntity<Void> debit(
            @PathVariable("userId") Long userId,
            @RequestParam("amount") Double amount) {

        walletService.debitWallet(userId, amount);
        return ResponseEntity.ok().build();
    }

    // Endpoint pour créditer (utile pour recharger son compte ou recevoir l'argent d'une vente)
    @PostMapping("/{userId}/credit")
    public ResponseEntity<Void> credit(@PathVariable Long userId, @RequestParam Double amount) {
        walletService.creditWallet(userId, amount);
        return ResponseEntity.ok().build();
    }
}