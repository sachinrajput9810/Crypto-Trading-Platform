package com.nt.service;

import com.nt.domain.OrderType;
import com.nt.model.Coin;
import com.nt.model.Order;
import com.nt.model.OrderItem;
import com.nt.model.User;

import java.util.List;

public interface OrderService {
    Order createOrder(User user , OrderItem orderItem , OrderType orderType);
    Order getOrderById(Long orderId);
    List<Order> getAllOrdersOfUser(Long userId , OrderType orderType , String assetSymbol) ;
    Order processOrder(Coin coin , double quantity , OrderType orderType , User user);
}
