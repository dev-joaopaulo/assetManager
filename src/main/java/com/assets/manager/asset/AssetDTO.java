package com.assets.manager.asset;

import com.assets.manager.asset_record.AssetRecord;
import com.assets.manager.broker.Broker;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder @AllArgsConstructor @NoArgsConstructor
public class AssetDTO {

    private Long id;
    private String assetClass;
    private String type;
    private String name;
    private String ticker;
    private int quantity;
    private float averagePrice;
    private float currentPrice;
    private Broker broker;
    private Set<AssetRecord> assetRecords;

    public static AssetDTO create(Asset asset){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(asset, AssetDTO.class);
    }

    public static Asset reverseMap(AssetDTO assetDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(assetDTO, Asset.class);
    }
}
