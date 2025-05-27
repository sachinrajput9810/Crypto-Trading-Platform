package com.nt.response;

import lombok.Data;

@Data
public class AuthResponse {
	
	private String jwt ;
	private Boolean status ;
	private String message ;
	private Boolean isTwoFactorAuthEnabled ;
	private String session ;
	
}
