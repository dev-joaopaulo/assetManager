package com.assets.manager.asset_record;

import com.assets.manager.asset.Asset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class AssetRecordDTO {

    private Long id;
    private Long assetId;
    private float averageCostPerShare;
    private int quantity;
    private String operationType; // buy or sell
    private LocalDate operationDate;


    public AssetRecordDTO(AssetRecord assetRecord) {
        this.id = assetRecord.getId();
        this.assetId = assetRecord.getAsset().getId();
        this.averageCostPerShare = assetRecord.getAverageCostPerShare();
        this.quantity = assetRecord.getQuantity();
        this.operationType = assetRecord.getOperationType();
        this.operationDate = assetRecord.getOperationDate();
    }

}
