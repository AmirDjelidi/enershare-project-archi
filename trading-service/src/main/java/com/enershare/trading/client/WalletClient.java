package com.enershare.trading.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


public interface WalletClient {

    @GetMapping("/wallets/{userId}/balance")
    Double getBalance(@PathVariable("userId") Long userId);

    @PostMapping("/wallets/{userId}/debit")
    void debitWallet(@PathVariable("userId") Long userId, @RequestParam Double amount);
}