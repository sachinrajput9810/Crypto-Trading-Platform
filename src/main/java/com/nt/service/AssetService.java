package com.nt.service;

import com.nt.model.Asset;
import com.nt.model.Coin;
import com.nt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetService{

    Asset createAsset(User user , Coin coin , double quantity);
    Asset getAssetById(Long assetId);
    Asset getAssetByUserIdAndId(Long userId, Long assetId);
    List<Asset> getUserAssets(Long userId);
    Asset updateAsset(Long assetId, double quantity);
    Asset findAssetByUserIdAndCoinId(Long userId, String coinId);
    void deleteAsset(Long assetId);

}


