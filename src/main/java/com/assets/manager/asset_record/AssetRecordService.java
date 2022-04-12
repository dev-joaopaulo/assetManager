package com.assets.manager.asset_record;

import com.assets.manager.asset.Asset;
import com.assets.manager.asset.AssetDTO;
import com.assets.manager.asset.AssetService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AssetRecordService {

    @Autowired
    private AssetRecordRepository assetRecordRepository;

    @Autowired
    private AssetService assetService;

    public List<AssetRecordDTO> getRecords(){
        return assetRecordRepository.findAll()
                .stream()
                .map(AssetRecordDTO::new)
                .collect(Collectors.toList());
    }


    public AssetRecord getById(Long id){
        return assetRecordRepository.findById(id).orElse(null);
    }


    public AssetRecordDTO insert(AssetRecordDTO assetRecordDTO){

        assetRecordDTO.setOperationDate(LocalDate.now());
        Asset asset = getAsset(assetRecordDTO.getAssetId());
        AssetRecord insertedRecord = assetRecordRepository.save(toEntity(assetRecordDTO, asset));

        updateConcernedAsset(insertedRecord);

        return new AssetRecordDTO(insertedRecord);
    }


    public void delete(Long id){
        assetRecordRepository.deleteById(id);
    }


    public AssetRecordDTO update(Long id, AssetRecordDTO assetRecordDTO){

        Assert.notNull(id, "Not possible to update object: null id asset record");

        AssetRecord dbAssetRecord = assetRecordRepository.getById(id);
        dbAssetRecord.setAsset(getAsset(assetRecordDTO.getAssetId()));
        dbAssetRecord.setAverageCostPerShare(assetRecordDTO.getAverageCostPerShare());
        dbAssetRecord.setQuantity(assetRecordDTO.getQuantity());
        dbAssetRecord.setOperationType(assetRecordDTO.getOperationType());
        dbAssetRecord.setOperationDate(assetRecordDTO.getOperationDate());

        dbAssetRecord = assetRecordRepository.save(dbAssetRecord);

        updateConcernedAsset(dbAssetRecord);

        return new AssetRecordDTO(dbAssetRecord);
    }


    public AssetRecord toEntity(AssetRecordDTO assetRecordDTO){
        return AssetRecord.builder()
                .id(assetRecordDTO.getId())
                .asset(new Asset())
                .averageCostPerShare(assetRecordDTO.getAverageCostPerShare())
                .quantity(assetRecordDTO.getQuantity())
                .operationType(assetRecordDTO.getOperationType())
                .operationDate(assetRecordDTO.getOperationDate())
                .build();
    }


    public AssetRecord toEntity(AssetRecordDTO assetRecordDTO, Asset asset) {
        AssetRecord assetRecord = this.toEntity(assetRecordDTO);
        assetRecord.setAsset(asset);
        return assetRecord;
    }


    private Asset getAsset(Long assetId){
        return assetService.getAssetById(assetId);
    }


    private void updateConcernedAsset(AssetRecord assetRecord) {

        Asset asset = assetRecord.getAsset();

        for(AssetRecord recordItem : asset.getAssetRecords()){
            if(Objects.equals(recordItem.getId(), assetRecord.getId())){
                asset.getAssetRecords().remove(recordItem);
                asset.getAssetRecords().add(assetRecord);
                assetService.updateAsset(asset.getId(), asset);
                return;
            }
        }
        asset.getAssetRecords().add(assetRecord);
        assetService.updateAsset(asset.getId(), asset);
    }

}
