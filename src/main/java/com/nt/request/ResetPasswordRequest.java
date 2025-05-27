package com.nt.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    private String otp ;
    private String Password ;
}
