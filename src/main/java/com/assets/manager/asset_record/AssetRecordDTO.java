package com.assets.manager.asset_record;

import com.assets.manager.asset.Asset;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.*;

@Data @Builder
public class AssetRecordDTO {

    private Long id;
    private Asset asset;
    private float averageCostPerShare;
    private int quantity;
    private String operationType; // buy or sell

    public static AssetRecordDTO create(AssetRecord assetRecord){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(assetRecord, AssetRecordDTO.class);
    }

    public static AssetRecord reverseMap(AssetRecordDTO assetRecordDTO){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(assetRecordDTO, AssetRecord.class);
    }

}
