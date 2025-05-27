package com.nt.model;

import com.nt.domain.OrderStatus;
import com.nt.domain.OrderType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    @ManyToOne
    private User user ;

    @Column(nullable = false)
    private OrderType orderType ;

    @Column(nullable = false)
    private BigDecimal price ;

    private LocalDateTime timeStamp  ;

    @Column(nullable = false)
    private OrderStatus status ;

    @OneToOne(mappedBy = "order"  , cascade = CascadeType.ALL)
    private OrderItem orderItem ;


}









