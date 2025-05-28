package com.nt.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class WatchList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    @ManyToMany
    private List<Coin> coins = new ArrayList<>();

    @OneToOne
    private User user ;

}
