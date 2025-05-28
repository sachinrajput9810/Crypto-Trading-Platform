package com.nt.controller;

import com.nt.model.PaymentDetails;
import com.nt.model.User;
import com.nt.service.PaymentDetailsService;
import com.nt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class PaymentDetailsController {

    @Autowired
    private UserService userService ;

    @Autowired
    private PaymentDetailsService paymentDetailsService ;

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(@RequestBody PaymentDetails paymentDetailsRequest ,
                                                            @RequestHeader("Authorization") String jwt){

        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.addPaymentDetails(
                paymentDetailsRequest.getAccountNumber() ,
                paymentDetailsRequest.getAccountHolderName() ,
                paymentDetailsRequest.getIfsc() ,
                paymentDetailsRequest.getBankName() ,
                user
        ) ;

        return new ResponseEntity<>(paymentDetails , HttpStatus.CREATED) ;
    }

    @GetMapping("/payment-details")
    public ResponseEntity<PaymentDetails> getUsersPaymentDetails(@RequestHeader("Authorization") String jwt){
        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.getPaymentDetails(user) ;
        return new ResponseEntity<>(paymentDetails , HttpStatus.OK) ;
    }

}
