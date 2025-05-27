package com.nt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nt.model.TwoFactorOTP;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOTP, String> {
	TwoFactorOTP findByUserId(Long userId) ;
}
