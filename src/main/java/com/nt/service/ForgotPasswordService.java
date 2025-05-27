package com.nt.service;

import com.nt.domain.VerificationType;
import com.nt.model.ForgotPasswordToken;
import com.nt.model.User;

public interface ForgotPasswordService {
    ForgotPasswordToken createToken(User user , String id ,String otp , VerificationType verificationType  , String sendTo) ;
    ForgotPasswordToken findById(String id);
    ForgotPasswordToken findByUser(Long userId);
    void deleteToken(ForgotPasswordToken token);
}
