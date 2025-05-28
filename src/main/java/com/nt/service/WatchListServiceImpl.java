package com.nt.service;

import com.nt.model.Coin;
import com.nt.model.User;
import com.nt.model.WatchList;
import com.nt.repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WatchListServiceImpl implements  WatchListService{

    @Autowired
    private WatchListRepository watchListRepository ;

    @Override
    public WatchList findUserWatchListByUserId(Long userId) {
        WatchList watchList = watchListRepository.findByUserId(userId) ;
        if(watchList == null){
            throw new RuntimeException("WatchList not found for user with id : " + userId) ;
        }
        return watchList ;
    }

    @Override
    public WatchList createWatchList(User user) {
        WatchList watchList = new WatchList() ;
        watchList.setUser(user);
        watchListRepository.save(watchList) ;
        return watchList ;
    }

    @Override
    public WatchList findById(Long id) {
        Optional<WatchList> optionalWatchList = watchListRepository.findById(id) ;
        if(optionalWatchList.isEmpty()){
            throw new RuntimeException("WatchList not found for id : " + id) ;
        }
        return optionalWatchList.get() ;
    }

    @Override
    public Coin addItemToWatchList(Coin coin, User user) {
        WatchList watchList = findUserWatchListByUserId(user.getId()) ;
        if(watchList.getCoins().contains(coin)){
            watchList.getCoins().remove(coin) ;
        }
        else watchList.getCoins().add(coin) ;
        watchListRepository.save(watchList) ;
        return coin ;
    }
}
