package com.assets.manager.domain;

import com.assets.manager.domain.dto.AssetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    public List<AssetDTO> getAssets(){
        return assetRepository.findAll().stream().map(AssetDTO::create).collect(Collectors.toList());
    }

    public Optional<AssetDTO> getAssetsById(Long id) {
        return assetRepository.findById(id).map(AssetDTO::create);
    }

    public Iterable<Asset> getAssetsByType(String type) {
        return assetRepository.findByType(type);
    }

    public AssetDTO insert(Asset asset) {
        Assert.isNull(asset.getId(), "It was not possible to insert record");
        return AssetDTO.create(assetRepository.save(asset));
    }

    public AssetDTO update(Long id, Asset asset) {
        Assert.notNull(id, "Not possible to update asset entry");

        Optional<AssetDTO> optionalAsset = getAssetsById(id);
        if(optionalAsset.isPresent()){
            Asset db = optionalAsset.map(a -> new Asset(a.getType(), a.getName())).get();
            db.setName(asset.getName());
            db.setType(asset.getType());
            System.out.println("Asset id updated " + db.getId());

            assetRepository.save(db);

            return AssetDTO.create(db);
        } else{
            throw new RuntimeException("Not possible to update asset entry");
        }
    }

    public void delete(Long id) {
        assetRepository.deleteById(id);
    }
}
