package com.nt.controller;

import com.nt.model.Coin;
import com.nt.model.User;
import com.nt.model.WatchList;
import com.nt.service.CoinService;
import com.nt.service.UserService;
import com.nt.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/watchlist")
@Controller
public class WatchListController {

    @Autowired
    private  WatchListService watchListService ;

    @Autowired
    private CoinService coinService ;

    @Autowired
    private UserService userService ;

    @GetMapping("/user")
    public ResponseEntity<WatchList> getUserWatchList(@RequestHeader("Authorization") String token){
        User user = userService.findUserProfileByJwt(token);
        WatchList watchList = watchListService.findUserWatchListByUserId(user.getId());
        return ResponseEntity.ok(watchList) ;
    }

    @GetMapping("/{watchListId}")
    public ResponseEntity<WatchList> getWatchListById(@PathVariable Long watchListId){
        WatchList watchList = watchListService.findById(watchListId);
        return ResponseEntity.ok(watchList) ;
    }

    @PostMapping("/add/coin/{coinId}")
    public ResponseEntity<Coin> addItemToWatchList(@RequestHeader("Authorization") String jwt ,
                                                   @PathVariable String coinId) throws Exception {

        User user = userService.findUserProfileByJwt(jwt) ;
        Coin coin = coinService.findById(coinId) ;
        Coin addedCoin = watchListService.addItemToWatchList(coin , user) ;
        return ResponseEntity.ok(addedCoin) ;
    }



}














