package com.nt.service;

import com.nt.model.Coin;

import java.util.List;

public interface CoinService {
    List<Coin> getCoinList(int page) throws Exception;
    String getMarketChart(String coinID , int days) throws Exception;
    String getCoinDetails(String coinID) throws Exception;
    Coin findById(String coinID) throws Exception;
    String searchCoin(String keyword) throws Exception;
    String getTop50CoinsByMarketCapRank() throws Exception;
    String getTradingCoins() throws Exception;
}
