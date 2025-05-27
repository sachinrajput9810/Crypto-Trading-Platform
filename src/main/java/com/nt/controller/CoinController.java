package com.nt.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nt.model.Coin;
import com.nt.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/coins")
@RestController
public class CoinController {
    @Autowired
    private CoinService coinService ;

    private ObjectMapper objectMapper ;

    @GetMapping("/{coinID}/chart")
    ResponseEntity<JsonNode> getMarketChart(@PathVariable String coinID ,
                                              @RequestParam("days") int days) throws Exception
    {
        String response = coinService.getMarketChart(coinID , days) ;
        JsonNode jsonNode = objectMapper.readTree(response) ;
        return new ResponseEntity<>(jsonNode , HttpStatus.ACCEPTED) ;
    }

    @GetMapping("/search")
    ResponseEntity<JsonNode> searchCoin(@RequestParam("q") String keyword) throws  Exception {
        String coin = coinService.searchCoin(keyword) ;
        JsonNode jsonNode = objectMapper.readTree(coin) ;
        return new ResponseEntity<>(jsonNode , HttpStatus.ACCEPTED) ;
    }

    @GetMapping("/top50")
    ResponseEntity<JsonNode> getTop50CoinsByMarketCapRank() throws Exception {
        String coin = coinService.getTop50CoinsByMarketCapRank() ;
        JsonNode jsonNode = objectMapper.readTree(coin) ;
        return new ResponseEntity<>(jsonNode , HttpStatus.ACCEPTED) ;
    }

    @GetMapping("/trading")
    ResponseEntity<JsonNode> getTradingCoins() throws Exception {
        String coin = coinService.getTradingCoins() ;
        JsonNode jsonNode = objectMapper.readTree(coin) ;
        return new ResponseEntity<>(jsonNode , HttpStatus.ACCEPTED) ;
    }

    @GetMapping("/details/{coinID}")
    ResponseEntity<JsonNode> getCoinDetails(@PathVariable("coinID") String coinID) throws Exception {
        String coin = coinService.getCoinDetails(coinID) ;
        JsonNode jsonNode = objectMapper.readTree(coin) ;
        return new ResponseEntity<>(jsonNode , HttpStatus.ACCEPTED) ;
    }

}













