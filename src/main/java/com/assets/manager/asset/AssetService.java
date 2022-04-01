package com.assets.manager.asset;

import com.assets.manager.broker.Broker;
import com.assets.manager.broker.BrokerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private BrokerRepository brokerRepository;

    public List<AssetDTO> getAssets(Pageable pageable){
        return assetRepository.
                findAll(pageable).
                stream().
                map(AssetDTO::create).collect(Collectors.toList());
    }

    public Optional<AssetDTO> getAssetsById(Long id) {

        return assetRepository.findById(id).map(AssetDTO::create);
    }

    public List<AssetDTO> getAssetsByType(String type) {
        return assetRepository.findByType(type)
                .stream()
                .map(AssetDTO::create)
                .collect(Collectors.toList());
    }

    public AssetDTO insert(Asset asset) {
        Assert.isNull(asset.getId(), "It was not possible to insert record");
        Asset savedAsset = assetRepository.save(asset);

        if(savedAsset.getBroker() != null){
            Broker broker = brokerRepository.getById(savedAsset.getBroker().getId());
            broker.getAssets().add(savedAsset);
            brokerRepository.save(broker);
        }

        return AssetDTO.create(savedAsset);
    }

    public AssetDTO update(Long id, AssetDTO asset) {
        Assert.notNull(id, "Not possible to update asset entry");

        Optional<Asset> optionalAsset = assetRepository.findById(id);
        if(optionalAsset.isPresent()){
            Asset db = optionalAsset.get();
            db.setCurrentPrice(asset.getCurrentPrice());

            return AssetDTO.create(assetRepository.save(db));
        } else{
            throw new RuntimeException("Not possible to update asset entry");
        }
    }

    public void delete(Long id) {
        assetRepository.deleteById(id);
    }

    private boolean isExistingAsset(Asset asset){
       List<Asset> existingAssets =  assetRepository.findByTicker(asset.getTicker());
       if(existingAssets.iterator().hasNext()){
           for (Asset a : existingAssets) {
               if(Objects.equals(asset.getBroker().getId(), a.getBroker().getId())){
                   return true;
               }
           }
       }
       return false;
    }
}
