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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
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
    private float totalCost;
    private Long brokerId;
    private List<Long> assetRecordsIds;


    public AssetDTO(Asset asset){
        this.id = asset.getId();
        this.assetClass = asset.getAssetClass();
        this.type = asset.getType();
        this.name = asset.getName();
        this.ticker = asset.getTicker();
        this.quantity = asset.getQuantity();
        this.totalCost = asset.getTotalCost();
        this.brokerId = asset.getBroker().getId();
        this.assetRecordsIds = getAssetRecordsIds(asset.getAssetRecords());
    }


    public float getAveragePrice() {
        return totalCost / quantity;
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

}
