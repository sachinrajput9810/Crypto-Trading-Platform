package com.nt.controller;

import com.nt.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.nt.config.JwtProvider;
import com.nt.model.TwoFactorOTP;
import com.nt.model.User;
import com.nt.repository.UserRepository;
import com.nt.response.AuthResponse;
import com.nt.service.CustomUserDetailService;
import com.nt.service.TwoFactorOTPServiceImpl;
import com.nt.utils.OtpUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository ;
	
	@Autowired
	private CustomUserDetailService userDetailService ;
	
	@Autowired
	private TwoFactorOTPServiceImpl twoFactorOTPServiceImpl ;

	@Autowired
	private EmailService emailService ;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> registerUser(@RequestBody User user) throws Exception{
		User isEmailExist = userRepository.findByEmail(user.getEmail()) ;
		if(isEmailExist != null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists with another user");
		}
		
		User newUser = new User() ;
		newUser.setEmail(user.getEmail());
		newUser.setPassword(user.getPassword());
		newUser.setFullName(user.getFullName()) ;
		User savedUser = userRepository.save(newUser) ;
		Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()) ;
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt = JwtProvider.generateToken(auth) ;
		
		AuthResponse res = new AuthResponse() ;
		res.setJwt(jwt) ;
		res.setMessage("Rgistration Success !!") ;
		res.setStatus(true) ;
		
		return new ResponseEntity<>(res , HttpStatus.CREATED) ;
	}
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception{
		
		String username = user.getEmail() ;
		String password = user.getPassword() ;
		
		Authentication auth = authenticate(username , password) ;
		
		
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt = JwtProvider.generateToken(auth) ;
		User authUser = userRepository.findByEmail(username) ;
		
		
		if(user.getTwoFactorAuth().getIsEnabled()) {
			AuthResponse res = new AuthResponse() ;
			res.setJwt(jwt) ;
			res.setMessage("Two Factor Auth is Enabled") ;
			res.setIsTwoFactorAuthEnabled(true) ; 
			String OTP = OtpUtils.generateOTP() ;
			
			TwoFactorOTP oldTwoFactorOTP = twoFactorOTPServiceImpl.findByUser(authUser.getId()) ;
			if(oldTwoFactorOTP != null) {
				twoFactorOTPServiceImpl.deleteTwoFactorOTP(oldTwoFactorOTP) ;
			}
			
			TwoFactorOTP newTwoFactorOTP = twoFactorOTPServiceImpl.createTwoFactorOTP(authUser, OTP, jwt) ;
			emailService.sendVerifcationOtpEmail(username, OTP) ;

			res.setSession(newTwoFactorOTP.getId()) ;
			return new ResponseEntity<>(res , HttpStatus.ACCEPTED) ;
		}
		
		AuthResponse res = new AuthResponse() ;
		res.setJwt(jwt) ;
		res.setMessage("Login Success !!") ;
		res.setStatus(true) ;
		
		return new ResponseEntity<>(res , HttpStatus.CREATED) ;
	}

	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = userDetailService.loadUserByUsername(username) ;
		
		if(userDetails == null) {
			throw new BadCredentialsException("Invalid Username !!");
		}
		
		if(!password.equals(userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Password") ;
		}
		return new UsernamePasswordAuthenticationToken(username , password , userDetails.getAuthorities()) ;
	}


	@PostMapping("/two-factor/otp/{otp}")
	public ResponseEntity<AuthResponse> verifySignInOTP(@PathVariable String otp , @RequestParam String id){
		TwoFactorOTP twoFactorOTP = twoFactorOTPServiceImpl.findById(id) ;
		if(twoFactorOTPServiceImpl.verifyTwoFactorOTP(twoFactorOTP, otp)) {
			AuthResponse res = new AuthResponse() ;
			res.setMessage("Two factor Authentication Success !!") ;
			res.setIsTwoFactorAuthEnabled(true);
			res.setJwt(twoFactorOTP.getJwt());
			return new ResponseEntity<>(res , HttpStatus.OK) ;
		}
		return null ;
	}
	
	
	
	
	
}
