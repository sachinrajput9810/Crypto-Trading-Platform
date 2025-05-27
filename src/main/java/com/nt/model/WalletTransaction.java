package com.nt.model;

import com.nt.domain.WalletTransactionType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class WalletTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    @ManyToOne
    private Wallet wallet ;

    private LocalDate localDate ;
    private WalletTransactionType type ;
    private String transferID ;
    private String purpose ;
    private Long amount ;


}
