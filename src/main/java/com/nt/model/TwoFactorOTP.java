package com.nt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class TwoFactorOTP {

	@Id
	private String id ;
	
	private String OTP ;
	
	@OneToOne
	@JsonProperty(access = Access.WRITE_ONLY)
	private User user ;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String jwt ;
}
