package com.assets.manager.asset_record;

import com.assets.manager.asset.Asset;
import com.assets.manager.asset.AssetRepository;
import com.assets.manager.types.OperationType;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetRecordService {

    @Autowired
    private AssetRecordRepository recordRepository;

    @Autowired
    private AssetRepository assetRepository;

    public List<AssetRecordDTO> getRecords(){
        return recordRepository.findAll()
                .stream()
                .map(AssetRecordDTO::create)
                .collect(Collectors.toList());
    }

    public AssetRecordDTO insert(AssetRecordDTO assetRecordDTO){

        Assert.notNull(assetRecordDTO.getAsset().getId(), "It was not possible to insert asset record");

        AssetRecord assetRecord = AssetRecordDTO.reverseMap(assetRecordDTO);
        AssetRecord insertedRecord = recordRepository.save(assetRecord);

        Asset asset = assetRepository.getById(assetRecord.getAsset().getId());
        asset.getAssetRecords().add(insertedRecord);
        assetRepository.save(asset);

        return AssetRecordDTO.create(insertedRecord);
    }
}
