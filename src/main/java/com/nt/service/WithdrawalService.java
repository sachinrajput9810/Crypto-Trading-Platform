package com.nt.service;

import com.nt.model.User;
import com.nt.model.Withdrawal;

import java.util.List;

public interface WithdrawalService {

    Withdrawal requestWithdrawal(Long amount , User user);
    Withdrawal processWithdrawal(Long withdrawalId , boolean accept) ;
    List<Withdrawal> getUserWithdrawalsHistory(User user) ;
    List<Withdrawal> getAllWithdrawalRequest() ;
}
