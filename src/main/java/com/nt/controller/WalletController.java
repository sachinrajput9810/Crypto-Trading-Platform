package com.nt.controller;

import com.nt.model.Order;
import com.nt.model.User;
import com.nt.model.Wallet;
import com.nt.model.WalletTransaction;
import com.nt.service.OrderService;
import com.nt.service.UserService;
import com.nt.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService ;

    @Autowired
    private UserService userService ;

    @Autowired
    private OrderService orderService ;



    @GetMapping("/api/wallet")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String token) {
        User user = userService.findUserProfileByJwt(token);
        Wallet wallet = walletService.getUserWallet(user);
        return ResponseEntity.ok(wallet);
    }

    @PutMapping("/api/wallet/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(@RequestHeader("Authorization") String jwt,
                                                         @PathVariable Long walletID ,
                                                         @RequestBody WalletTransaction req)
    {
        User senderUser = userService.findUserProfileByJwt(jwt);
        Wallet recieverWallet = walletService.findWalletById(walletID);
        Wallet wallet = walletService.walletToWalletTransfer(senderUser,recieverWallet , BigDecimal.valueOf(req.getAmount()));
        return ResponseEntity.ok(wallet);

    }

    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(@RequestHeader("Authorization") String jwt,
                                                         @PathVariable Long walletID ,
                                                         @RequestBody WalletTransaction req)
    {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(walletID);
        Wallet wallet = walletService.payOrderPayment(order , user);
        return new ResponseEntity<>(wallet, HttpStatus.OK) ;
    }


}

