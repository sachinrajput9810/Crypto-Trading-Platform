package com.nt.controller;

import com.nt.domain.VerificationType;
import com.nt.model.ForgotPasswordToken;
import com.nt.model.User;
import com.nt.model.VerificationCode;
import com.nt.request.ForgotPasswordTokenRequest;
import com.nt.request.ResetPasswordRequest;
import com.nt.response.ApiResponse;
import com.nt.response.AuthResponse;
import com.nt.service.EmailService;
import com.nt.service.ForgotPasswordService;
import com.nt.service.UserService;
import com.nt.service.VerificationCodeService;
import com.nt.utils.OtpUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {
    @Autowired
    private UserService userService ;

    @Autowired
    private EmailService emailService ;

    @Autowired
    private VerificationCodeService verificationCodeService  ;

    @Autowired
    private ForgotPasswordService forgotPasswordService ;


    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfileByJwt(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserProfileByJwt(jwt) ;
        return new ResponseEntity<User>(user , HttpStatus.OK) ;
    }

    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOTP(@RequestHeader("Authorization") String jwt ,
                                                    @PathVariable VerificationType verificationType) throws MessagingException {
        User user = userService.findUserProfileByJwt(jwt) ;
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId()) ;
        if(verificationCode == null){
            verificationCode = verificationCodeService.sendVerificationCode(user , verificationType) ;
        }
        if(verificationType.equals(VerificationType.EMAIL)){
            emailService.sendVerifcationOtpEmail(user.getEmail() , verificationCode.getOtp()) ;
        }
        return new ResponseEntity<String>("OTP sent successfully" , HttpStatus.OK) ;
    }


    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthorization(@RequestHeader("Authorization") String jwt ,
                                                             @PathVariable String otp) throws Exception {
        User user = userService.findUserProfileByJwt(jwt) ;
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId()) ;
        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL)
                            ?verificationCode.getEmail():verificationCode.getMobile() ;
        boolean isVerified = verificationCode.getOtp().equals(otp) ;

        if(isVerified){
            User updatedUser = userService.enableTwoFactorAuthentication(verificationCode.getVerificationType()
                                                                         , sendTo , user) ;
            verificationCodeService.deleteVerificationCode(verificationCode) ;
            return new ResponseEntity<User>(updatedUser , HttpStatus.OK) ;
        }
        throw new Exception("Wrong OTP !!") ;
    }

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOTP(
                                                        @RequestBody ForgotPasswordTokenRequest req) throws MessagingException {

        User user = userService.findUserByEmail(req.getSendTo()) ;
        String otp = OtpUtils.generateOTP() ;
        UUID uuid = UUID.randomUUID() ;
        String id = uuid.toString() ;

        ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId()) ;
        if(token == null){
            token = forgotPasswordService.createToken(user , id , otp , req.getVerificationType() ,req.getSendTo() ) ;
        }

        if ((req.getVerificationType().equals(VerificationType.EMAIL))){
            emailService.sendVerifcationOtpEmail(user.getEmail() , token.getOtp()); ;
        }

        AuthResponse response = new AuthResponse() ;
        response.setSession(token.getId());
        response.setMessage("Password Reset OTP Send successfully");

        return new ResponseEntity<AuthResponse>(response , HttpStatus.OK) ;
    }

    @PatchMapping("/api/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String id ,
                                              @RequestBody ResetPasswordRequest req ,
                                              @RequestHeader("Authorization") String jwt ) throws Exception {

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id) ;
        boolean isVerified = forgotPasswordToken.getOtp().equals(req.getOtp()) ;

        if(isVerified){
            userService.updatePassword(forgotPasswordToken.getUser() , req.getPassword());
            ApiResponse response = new ApiResponse() ;
            response.setMessage("Password reset successfully");
            return new ResponseEntity<ApiResponse>(response , HttpStatus.OK) ;
        }
        throw new Exception("Invalid OTP") ;
    }



}
