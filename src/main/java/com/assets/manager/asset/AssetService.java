package com.assets.manager.asset;

import com.assets.manager.asset_record.AssetRecord;
import com.assets.manager.broker.Broker;
import com.assets.manager.broker.BrokerRepository;
import com.assets.manager.types.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Assert.isNull(asset.getId(), "It was not possible to insert record: null Asset");
        Assert.isTrue(!isExistingAsset(asset), "It was not possible to insert record: duplicated Asset");

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
            Asset dbAsset = optionalAsset.get();
            dbAsset.setAssetRecords(updateAssetRecordList(dbAsset.getAssetRecords(), asset.getAssetRecords()));
            dbAsset.setTotalCost(updateAssetTotalCost(asset));
            dbAsset.setQuantity(updateAssetQuantity(asset));

            return AssetDTO.create(assetRepository.save(dbAsset));
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

    private int updateAssetQuantity(AssetDTO assetDTO){
        int updatedQuantity = 0;
        Set<AssetRecord> assetRecordList = assetDTO.getAssetRecords();
        for (AssetRecord assetRecord: assetRecordList) {
            if(Objects.equals(assetRecord.getOperationType(), OperationType.BUY.toString())){
                updatedQuantity += assetRecord.getQuantity();
            } else {
                updatedQuantity -= assetRecord.getQuantity();
            }
        }
        return  updatedQuantity;
    }

    private float updateAssetTotalCost(AssetDTO assetDTO){
        float updatedTotalCost = 0F;
        Set<AssetRecord> assetRecordList = assetDTO.getAssetRecords();
        for (AssetRecord assetRecord: assetRecordList) {
            if(Objects.equals(assetRecord.getOperationType(), OperationType.BUY.toString())){
                updatedTotalCost += assetRecord.getQuantity() * assetRecord.getAverageCostPerShare();
            } else {
                updatedTotalCost -= assetRecord.getQuantity() * assetDTO.getAveragePrice();
            }
        }
        return  updatedTotalCost;
    }

    private Set<AssetRecord> updateAssetRecordList(Set<AssetRecord> existingList, Set<AssetRecord> updatedList){
        return new HashSet<>(Stream.concat(existingList.stream(), updatedList.stream()).collect(Collectors.toList()));
    }


}
