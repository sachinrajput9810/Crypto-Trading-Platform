package com.nt.service;

import jakarta.mail.MessagingException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	
	private JavaMailSender javaMailSender ;

	public void sendVerifcationOtpEmail(String email , String otp) throws MessagingException {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage() ;
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage , "utf-8") ;
		String subject = "Verify OTP" ;
		String text =  "Your Verification OTP is : "+otp ;

		mimeMessageHelper.setText(text );
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setTo(email);

		try{
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new MailSendException(e.getMessage()) ;
		}
	}
	
}
