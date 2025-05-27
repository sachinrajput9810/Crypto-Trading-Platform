package com.nt.service;

import com.nt.model.Order;
import com.nt.model.User;
import com.nt.model.Wallet;

import java.math.BigDecimal;

public interface WalletService {
    Wallet getUserWallet(User user) ;
    Wallet addBalanceToWallet(Wallet wallet , BigDecimal amount) ;
    Wallet findWalletById(Long id) ;
    Wallet walletToWalletTransfer(User sender , Wallet receiverWallet , BigDecimal amount) ;
    Wallet payOrderPayment(Order order , User user) ;
}
