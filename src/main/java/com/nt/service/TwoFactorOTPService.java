package com.nt.service;

import com.nt.model.TwoFactorOTP;
import com.nt.model.User;

public interface TwoFactorOTPService {

	TwoFactorOTP createTwoFactorOTP(User user , String otp , String jwt) ;
	TwoFactorOTP findByUser(Long userId) ;
	TwoFactorOTP findById(String id) ;
	Boolean verifyTwoFactorOTP(TwoFactorOTP twoFactorOTP , String otp) ;
	void deleteTwoFactorOTP(TwoFactorOTP twoFactorOTP) ;
	
}
