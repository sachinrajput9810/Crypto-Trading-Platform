package com.nt.model;

import com.nt.domain.VerificationType;

import lombok.Data;

@Data
public class TwoFactorAuth {
	
	private Boolean isEnabled = false ;
	private VerificationType sendTo ;
}
