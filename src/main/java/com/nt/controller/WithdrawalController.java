package com.nt.controller;

import com.nt.model.User;
import com.nt.model.Wallet;
import com.nt.model.Withdrawal;
import com.nt.service.UserService;
import com.nt.service.WalletService;
import com.nt.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/api/withdrawal")
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService ;

    @Autowired
    private UserService userService ;

    @Autowired
    private WalletService walletService ;

//    @Autowired
//    private WalletTransactionService walletTransactionService ;

    public ResponseEntity<Withdrawal> withdrawalRequest(@PathVariable Long amount ,
                                                        @RequestHeader("Authorization") String jwt)
    {
        User user = userService.findUserProfileByJwt(jwt);
        Wallet userWallet = walletService.getUserWallet(user);

        Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount , user);
        walletService.addBalanceToWallet(userWallet , BigDecimal.valueOf(-withdrawal.getAmount()));

         // code
        return new ResponseEntity<>(withdrawal , HttpStatus.OK) ;
    }

    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(@PathVariable Long id ,
                                               @PathVariable Boolean accept ,
                                               @RequestHeader("Authorization") String jwt)

    {
        User user = userService.findUserProfileByJwt(jwt);
        Withdrawal withdrawal = withdrawalService.processWithdrawal(id , accept);

        Wallet userWallet = walletService.getUserWallet(user);
        if(!accept) {
            walletService.addBalanceToWallet(userWallet, BigDecimal.valueOf(withdrawal.getAmount()));
        }
        return new ResponseEntity<>(withdrawal , HttpStatus.OK) ;

    }

    @GetMapping("/api/withdrawal")
    public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(@RequestHeader("Authorization") String jwt)
    {
        User user = userService.findUserProfileByJwt(jwt);
        List<Withdrawal> withdrawals = withdrawalService.getUserWithdrawalsHistory(user);
        return new ResponseEntity<>(withdrawals , HttpStatus.OK) ;
    }

    @GetMapping("/api/admin/withdrawal ")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserProfileByJwt(jwt);
        List<Withdrawal> withdrawals = withdrawalService.getAllWithdrawalRequest();
        return new ResponseEntity<>(withdrawals , HttpStatus.OK) ;
    }



}


















