package com.nt.service;

import com.nt.model.PaymentDetails;
import com.nt.model.User;

public interface PaymentDetailsService {
    public PaymentDetails addPaymentDetails(String accountNumber ,
                                            String accountHolder ,
                                            String ifsc ,
                                            String bankName,
                                            User user);

    public PaymentDetails getPaymentDetails(User user) ;
}
