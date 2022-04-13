package com.assets.manager;

import com.assets.manager.asset.Asset;
import com.assets.manager.asset.AssetDTO;
import com.assets.manager.asset.AssetService;
import com.assets.manager.asset_record.AssetRecord;
import com.assets.manager.asset_record.AssetRecordDTO;
import com.assets.manager.asset_record.AssetRecordService;
import com.assets.manager.broker.Broker;
import com.assets.manager.broker.BrokerDTO;
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

    public AssetDTO insertFakeAsset(String name, String type, Long brokerId) {
        AssetDTO assetDTO = new AssetDTO();
        assetDTO.setName(name);
        assetDTO.setType(type);
        assetDTO.setBrokerId(brokerId);

        return assetService.insert(assetDTO);
    }

    public BrokerDTO insertFakeBroker(String brokerName){
        Broker broker = new Broker();
        broker.setName(brokerName);
        return brokerService.insert(new BrokerDTO(broker));
    }

    public AssetRecordDTO insertFakeAssetRecord(Long assetId, int quantity,
                                                 float averageCost, OperationType operationType){
        AssetRecordDTO assetRecordDTO = AssetRecordDTO.builder()
                .assetId(assetId)
                .operationType(operationType.toString())
                .quantity(quantity)
                .averageCostPerShare(averageCost)
                .build();

        return assetRecordService.insert(assetRecordDTO);
    }

}
