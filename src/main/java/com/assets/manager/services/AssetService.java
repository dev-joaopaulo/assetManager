package com.assets.manager.services;

import com.assets.manager.models.Asset;
import com.assets.manager.repositories.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    public List<Asset> getAssets(){
        return assetRepository.findAll();
    }

    public Optional<Asset> getAssetsById(Long id) {
        return assetRepository.findById(id);
    }

    public Iterable<Asset> getAssetsByType(String type) {
        return assetRepository.findByType(type);
    }

    public Asset insert(Asset asset) {
        Assert.isNull(asset.getId(), "It was not possible to insert record");
        return assetRepository.save(asset);
    }

    public Asset update(Long id, Asset asset) {
        Assert.notNull(id, "Not possible to update asset entry");

        Optional<Asset> optionalAsset = getAssetsById(id);
        if(optionalAsset.isPresent()){
            Asset db = optionalAsset.get();
            db.setName(asset.getName());
            db.setType(asset.getType());

            return assetRepository.save(db);
        } else{
            throw new RuntimeException("Not possible to update asset entry");
        }
    }

    public void delete(Long id) {
        assetRepository.deleteById(id);
    }
}