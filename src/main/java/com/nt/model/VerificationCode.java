package com.nt.model;

import com.nt.domain.VerificationType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class VerificationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    private String otp ;
    private String email ;
    private String mobile ;

    @OneToOne
    private User user ;
    private String code ;
    private VerificationType verificationType ;
}
