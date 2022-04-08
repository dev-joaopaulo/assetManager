package com.assets.manager;

import com.assets.manager.asset.Asset;
import com.assets.manager.asset.AssetDTO;
import com.assets.manager.asset.AssetService;
import com.assets.manager.asset_record.AssetRecord;
import com.assets.manager.asset_record.AssetRecordDTO;
import com.assets.manager.asset_record.AssetRecordService;
import com.assets.manager.broker.Broker;
import com.assets.manager.broker.BrokerService;
import com.assets.manager.types.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MockDataService {

    @Autowired
    private AssetRecordService assetRecordService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private BrokerService brokerService;

    public AssetDTO insertFakeAsset(String name, String type){
        Asset asset = new Asset();
        asset.setName(name);
        asset.setType(type);
        asset.setBroker(insertFakeBroker("Clear"));

        return assetService.insert(asset);
    }

    public Broker insertFakeBroker(String brokerName){
        Broker broker = new Broker();
        broker.setName(brokerName);
        return brokerService.insert(broker);
    }

    public AssetRecordDTO insertFakeAssetRecord(AssetDTO assetDto, int quantity,
                                                 float averageCost, OperationType operationType){
        Asset assetReturned = AssetDTO.reverseMap(assetDto);
        AssetRecord assetRecord = AssetRecord.builder()
                .asset(assetReturned)
                .operationType(operationType.toString())
                .quantity(quantity)
                .averageCostPerShare(averageCost)
                .build();

        return assetRecordService.insert(AssetRecordDTO.create(assetRecord));
    }

}
