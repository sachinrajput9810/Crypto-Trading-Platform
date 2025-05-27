package com.nt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.nt.domain.USER_ROLE;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String fullName ;
	private String email ;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password ;
	
	private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER ;
	
	@Embedded
	private TwoFactorAuth twoFactorAuth = new TwoFactorAuth() ;
	
}
