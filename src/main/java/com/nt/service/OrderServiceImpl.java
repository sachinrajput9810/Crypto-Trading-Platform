package com.nt.service;

import com.nt.domain.OrderStatus;
import com.nt.domain.OrderType;
import com.nt.model.*;
import com.nt.repository.OrderItemRepository;
import com.nt.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository ;

    @Autowired
    private WalletService walletService ;

    @Autowired
    private OrderItemRepository orderItemRepository ;

    @Autowired
    private AssetService assetService ;

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        double price = orderItem.getCoin().getCurrentPrice()*orderItem.getQuantity();
        Order order = new Order() ;
        order.setOrderType(orderType);
        order.setPrice(BigDecimal.valueOf(price));
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setTimeStamp(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        return orderRepository.save(order) ;
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> getAllOrdersOfUser(Long userId, OrderType orderType, String assetSymbol) {
        return orderRepository.findByUserId(userId);
    }

    private OrderItem createOrderItem(Coin coin, double quantity , double buyPrice, double sellPrice) {
        OrderItem orderItem = new OrderItem();
        orderItem.setCoin(coin);
        orderItem.setQuantity(quantity);
        orderItem.setBuyPrice(buyPrice);
        orderItem.setSellPrice(sellPrice);
        return orderItemRepository.save(orderItem) ;
    }

    @Transactional
    public Order buyAsset(Coin coin, double quantity, User user) {
        if(quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }
        double buyPrice = coin.getCurrentPrice() ;

        OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, 0);
        Order order = createOrder(user, orderItem, OrderType.BUY);
        orderItem.setOrder(order);

        walletService.payOrderPayment(order, user);
        order.setStatus(OrderStatus.SUCCESSFUL);
        order.setOrderType(OrderType.BUY);
        Order savedOrder = orderRepository.save(order);

        // create asset
        Asset oldAsset = assetService.findAssetByUserIdAndCoinId(order.getUser().getId(),
                                                                 order.getOrderItem().getCoin().getId()
                                                                 );
        if (oldAsset == null) {
            assetService.createAsset(user , orderItem.getCoin() , orderItem.getQuantity() ) ;
        }
        else{
            assetService.updateAsset(oldAsset.getId() , quantity) ;
        }

        return savedOrder ;
    }

    @Transactional
    public Order sellAsset(Coin coin, double quantity, User user) {
        if(quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }
        double sellPrice = coin.getCurrentPrice() ;
        Asset assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId() , coin.getId()) ;
        double buyPrice = assetToSell.getBuyPrice();
        if(assetToSell != null) {
            OrderItem orderItem = createOrderItem(coin, quantity, buyPrice, sellPrice);
            Order order = createOrder(user, orderItem, OrderType.SELL);

            orderItem.setOrder(order);

            if (assetToSell.getQuantity() >= quantity) {
                order.setStatus(OrderStatus.SUCCESSFUL);
                order.setOrderType(OrderType.SELL);
                Order savedOrder = orderRepository.save(order);
                walletService.payOrderPayment(order, user);

                Asset updatedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);
                if (updatedAsset.getQuantity() * coin.getCurrentPrice() <= 1) {
                    assetService.deleteAsset(updatedAsset.getId());
                }
                return savedOrder;
            }
            throw new RuntimeException("You don't have enough quantity to sell");
        }
        throw new RuntimeException("You don't have this asset to sell");
    }

    @Override
    @Transactional
    public Order processOrder(Coin coin, double quantity, OrderType orderType, User user) {
        if(orderType.equals(OrderType.BUY)){
            return buyAsset(coin, quantity, user);
        }
        else if(orderType.equals(OrderType.SELL)){
            return sellAsset(coin, quantity, user);
        }
        else{
            throw new RuntimeException("Invalid order type");
        }
    }
}
