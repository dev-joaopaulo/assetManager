package com.assets.manager.asset_record;

import com.assets.manager.asset.Asset;
import com.assets.manager.asset.AssetRepository;
import com.assets.manager.types.OperationType;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssetRecordService {

    @Autowired
    private AssetRecordRepository recordRepository;

    @Autowired
    private AssetRepository assetRepository;

    public AssetRecordDTO insert(AssetRecord assetRecord){

        Assert.isNull(assetRecord.getAsset().getId(), "It was not possible to insert asset record");
        AssetRecord insertedRecord = recordRepository.save(assetRecord);

        Asset asset = assetRepository.getById(assetRecord.getAsset().getId());
        asset.getAssetRecords().add(insertedRecord);
        assetRepository.save(asset);

        return AssetRecordDTO.create(insertedRecord);
    }
}
