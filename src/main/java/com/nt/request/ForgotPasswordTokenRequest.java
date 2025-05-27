package com.nt.request;

import com.nt.domain.VerificationType;
import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {
    private String sendTo ;
    private VerificationType verificationType;
}
