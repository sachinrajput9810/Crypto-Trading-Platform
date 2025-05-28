package com.nt.service;

import com.nt.model.Coin;
import com.nt.model.User;
import com.nt.model.WatchList;

public interface WatchListService {
    WatchList findUserWatchListByUserId(Long userId);
    WatchList createWatchList(User user) ;
    WatchList findById(Long id);

    Coin addItemToWatchList(Coin coin , User user);
}
