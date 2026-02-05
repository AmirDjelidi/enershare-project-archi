package com.enershare.wallet.controller;

import com.enershare.wallet.domain.Wallet;
import com.enershare.wallet.repository.WalletRepository;
import com.enershare.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;
    private final WalletRepository walletRepository;
    @GetMapping("/{userId}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable Long userId) {
        return ResponseEntity.ok(walletService.getBalance(userId));
    }

    @PostMapping("/{userId}/newWallet")
    public ResponseEntity<Wallet> createWallet(@PathVariable("userId")Long userId){
        Wallet wallet = Wallet.builder().
                userId(userId).
                balance(0.0).
                build();
        return ResponseEntity.ok(walletRepository.save(wallet));
    }
    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<Void> debit(
            @PathVariable("userId") Long userId,
            @RequestParam("amount") Double amount) {

        walletService.debitWallet(userId, amount);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{userId}/addFunds")
    public ResponseEntity<Void> credit(@PathVariable Long userId, @RequestParam Double amount) {
        walletService.creditWallet(userId, amount);
        return ResponseEntity.ok().build();
    }
}