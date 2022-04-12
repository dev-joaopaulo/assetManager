package com.assets.manager.asset;

import com.assets.manager.asset_record.AssetRecord;
import com.assets.manager.asset_record.AssetRecordRepository;
import com.assets.manager.broker.BrokerService;
import com.assets.manager.types.OperationType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AssetService {

    private AssetRepository assetRepository;

    private BrokerService brokerService;

    private AssetRecordRepository assetRecordRepository;

    public AssetService(AssetRepository assetRepository, BrokerService brokerService, AssetRecordRepository assetRecordRepository) {
        this.assetRepository = assetRepository;
        this.brokerService = brokerService;
        this.assetRecordRepository = assetRecordRepository;
    }

    public List<AssetDTO> getAssets(Pageable pageable){
        return assetRepository.
                findAll(pageable).
                stream().
                map(AssetDTO::new).collect(Collectors.toList());
    }


    public Asset getAssetById(Long id) {
        return assetRepository.findById(id).orElse(null);
    }


    public List<AssetDTO> getAssetsByType(String type) {
        return assetRepository.findByType(type)
                .stream()
                .map(AssetDTO::new)
                .collect(Collectors.toList());
    }


    public AssetDTO insert(AssetDTO assetDTO) {
        Asset asset = toEntity(assetDTO);
        Asset savedAsset = assetRepository.save(asset);

        brokerService.addAssetToBroker(assetDTO.getBrokerId(), savedAsset);

        return new AssetDTO(savedAsset);
    }


    public AssetDTO update(Long id, AssetDTO asset) {
        Assert.notNull(id, "Not possible to update asset entry");

        Asset dbAsset = getAssetById(id);
        dbAsset.setName(asset.getName());
        dbAsset = updateQuantityAndCost(dbAsset);
        Set<AssetRecord> updatedAssetRecords = updateAssetRecords(new AssetDTO(dbAsset),asset);
        dbAsset.setAssetRecords(updatedAssetRecords);
        return new AssetDTO(assetRepository.save(dbAsset));
    }


    public void updateAsset(Long id, Asset asset) {
        Asset dbAsset = assetRepository.getById(id);
        dbAsset.setName(asset.getName());
        dbAsset.setBroker(asset.getBroker());
        dbAsset.setTicker(asset.getTicker());
        dbAsset.setAssetRecords(asset.getAssetRecords());
        dbAsset = updateQuantityAndCost(dbAsset);
        assetRepository.save(dbAsset);
    }


    public void delete(Long id) {
        Asset asset = assetRepository.getById(id);
        Assert.notNull(asset.getBroker(), "Forbidden - Asset does not have a Broker");
        asset.getBroker().getAssets().remove(asset);
        assetRepository.deleteById(id);
    }

    public Asset toEntity(AssetDTO assetDTO){
        return Asset.builder()
                .id(assetDTO.getId())
                .assetClass(assetDTO.getAssetClass())
                .type(assetDTO.getType())
                .name(assetDTO.getName())
                .ticker(assetDTO.getTicker())
                .quantity(assetDTO.getQuantity())
                .totalCost(assetDTO.getTotalCost())
                .broker(brokerService.getBrokerById(assetDTO.getBrokerId()))
                .assetRecords(new HashSet<>())
                .build();
    }


    public Asset toEntity(AssetDTO assetDTO, Set<AssetRecord> assetRecords){
        Asset asset = toEntity(assetDTO);
        asset.setAssetRecords(assetRecords);
        return asset;
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


    public Asset updateQuantityAndCost(Asset asset){
        int qty = 0;
        float totalCost = 0;
        Set<AssetRecord> assetRecordList = asset.getAssetRecords();
        for (AssetRecord assetRecord: assetRecordList) {
            if(Objects.equals(assetRecord.getOperationType(), OperationType.BUY.toString())){
                qty += assetRecord.getQuantity();
                totalCost += assetRecord.getQuantity() * assetRecord.getAverageCostPerShare();
            } else if(Objects.equals(assetRecord.getOperationType(), OperationType.SELL.toString())){
                qty -= assetRecord.getQuantity();
                totalCost -= assetRecord.getQuantity() * asset.getAveragePrice();
            }
        }
        asset.setQuantity(qty);
        asset.setTotalCost(totalCost);
        return asset;
    }

    private Set<AssetRecord> getAssetRecordsFromIds(List<Long> ids){
        Set<AssetRecord> assetRecords = new HashSet<>();
        for (Long id: ids) {
            assetRecords.add(assetRecordRepository.getById(id));
        }
        return assetRecords;
    }

    private Set<AssetRecord> updateAssetRecords(AssetDTO dbAsset, AssetDTO asset){
        Set<AssetRecord> existingList = getAssetRecordsFromIds(dbAsset.getAssetRecordsIds());
        Set<AssetRecord> updatedList = getAssetRecordsFromIds(asset.getAssetRecordsIds());
        return Stream.concat(existingList.stream(), updatedList.stream()).collect(Collectors.toSet());
    }

}
