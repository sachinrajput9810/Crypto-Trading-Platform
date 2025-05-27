package com.nt.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.model.TwoFactorOTP;
import com.nt.model.User;
import com.nt.repository.TwoFactorOtpRepository;

@Service
public class TwoFactorOTPServiceImpl implements TwoFactorOTPService{

	@Autowired
	private TwoFactorOtpRepository twoFactorOtpRepository ;
	
	@Override
	public TwoFactorOTP createTwoFactorOTP(User user, String otp, String jwt) {
		UUID uuid = UUID.randomUUID() ;
		String id = uuid.toString() ;
		
		TwoFactorOTP twoFactorOTP = new TwoFactorOTP() ;
		twoFactorOTP.setId(id);
		twoFactorOTP.setOTP(otp);
		twoFactorOTP.setUser(user);
		twoFactorOTP.setJwt(jwt);
		return twoFactorOtpRepository.save(twoFactorOTP) ;
 	}

	@Override
	public TwoFactorOTP findByUser(Long userId) {
		return twoFactorOtpRepository.findByUserId(userId) ; 
	}

	@Override
	public TwoFactorOTP findById(String id) {
		Optional<TwoFactorOTP> opt = twoFactorOtpRepository.findById(id) ;
		return opt.orElse(null) ;
 	}

	@Override
	public Boolean verifyTwoFactorOTP(TwoFactorOTP twoFactorOTP, String otp) {
		return otp.equals(twoFactorOTP.getOTP()) ;
	}

	@Override
	public void deleteTwoFactorOTP(TwoFactorOTP twoFactorOTP) {
		twoFactorOtpRepository.delete(twoFactorOTP);
	}

}
