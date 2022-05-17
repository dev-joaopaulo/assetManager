package com.assets.manager.asset;

import com.assets.manager.asset_record.AssetRecord;
import com.assets.manager.broker.Broker;
import com.assets.manager.broker.BrokerDTO;
import com.assets.manager.broker.BrokerService;
import com.assets.manager.types.OperationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class AssetDTO {

    private Long id;
    private String assetClass;
    private String type;
    private String name;
    private String ticker;
    private int quantity;
    private float averagePrice;
    private Long brokerId;
    private List<Long> assetRecordsIds;


    public AssetDTO(Asset asset){
        this.id = asset.getId();
        this.assetClass = asset.getAssetClass();
        this.type = asset.getType();
        this.name = asset.getName();
        this.ticker = asset.getTicker();
        this.brokerId = asset.getBroker().getId();
        this.assetRecordsIds = getAssetRecordsIds(asset.getAssetRecords());
        this.averagePrice = getAveragePrice(asset.getAssetRecords());
        this.quantity = getQuantity(asset.getAssetRecords());
    }


    private List<Long> getAssetRecordsIds(Set<AssetRecord> assetRecords) {
        if(assetRecords == null){
            return new ArrayList<>();
        }
        return assetRecords
                .stream()
                .map(AssetRecord::getId)
                .collect(Collectors.toList());
    }

    private float getAveragePrice(Set<AssetRecord> assetRecordList){
        int qty = 0;
        float cost = 0;
        for (AssetRecord assetRecord: assetRecordList) {
            if(Objects.equals(assetRecord.getOperationType().toLowerCase(), OperationType.BUY.toString())){
                qty += assetRecord.getQuantity();
                cost += assetRecord.getQuantity() * assetRecord.getAverageCostPerShare();
            }
        }
        return qty > 0 ? cost/qty : 0;
    }

    private int getQuantity(Set<AssetRecord> assetRecordList) {
        int qty = 0;
        for (AssetRecord assetRecord : assetRecordList) {
            if (Objects.equals(assetRecord.getOperationType().toLowerCase(), OperationType.BUY.toString())) {
                qty += assetRecord.getQuantity();
            } else if (Objects.equals(assetRecord.getOperationType().toLowerCase(), OperationType.SELL.toString())) {
                qty -= assetRecord.getQuantity();
            }
        }
        return qty;
    }

}
