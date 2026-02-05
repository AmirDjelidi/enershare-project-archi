package com.enershare.wallet.service;

import com.enershare.wallet.domain.Wallet;
import com.enershare.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Double getBalance(Long userId) {
        return walletRepository.findByUserId(userId)
                .map(Wallet::getBalance)
                .orElseThrow(() -> new RuntimeException("Wallet not found for the user : " + userId));
    }

    @Transactional
    public void debitWallet(Long userId, Double amount) {
        Wallet wallet = walletRepository.findWithLockByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        if (wallet.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance!");
        }

        wallet.setBalance(wallet.getBalance() - amount);
        walletRepository.save(wallet);
    }

    @Transactional
    public void creditWallet(Long userId, Double amount) {
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setBalance(wallet.getBalance() + amount);
        walletRepository.save(wallet);
    }
}
