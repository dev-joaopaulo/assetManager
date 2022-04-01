package com.assets.manager;

import com.assets.manager.asset.Asset;
import com.assets.manager.asset.AssetDTO;
import com.assets.manager.asset.AssetService;
import com.assets.manager.asset_record.AssetRecord;
import com.assets.manager.asset_record.AssetRecordDTO;
import com.assets.manager.asset_record.AssetRecordService;
import com.assets.manager.types.OperationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@Transactional
class AssetRecordTest {

    @Autowired
    private AssetRecordService assetRecordService;

    @Autowired
    private AssetService assetService;

    @Test
    void insertTest(){

        Asset asset = new Asset();
        asset.setName("assetNameTest");
        asset.setType("assetTypeTest");

        AssetDTO assetDtoReturned = assetService.insert(asset);
        Asset assetReturned = AssetDTO.reverseMap(assetDtoReturned);

        assertNotNull(assetReturned);
        assertEquals(0, assetReturned.getAssetRecords().size());

        AssetRecord assetRecord = AssetRecord.builder()
                .asset(assetReturned)
                .operationType(OperationType.BUY.toString())
                .quantity(100)
                .averageCostPerShare(150.0F)
                .build();

        AssetRecordDTO insertedAssetRecordDto =
                assetRecordService.insert(AssetRecordDTO.create(assetRecord));

        assertNotNull(insertedAssetRecordDto.getId());
        assertEquals(100, insertedAssetRecordDto.getQuantity() );
        assertEquals(150.0F, insertedAssetRecordDto.getAverageCostPerShare(), 0.005F);
        assertEquals(LocalDate.now(), insertedAssetRecordDto.getOperationDate());

        Optional<AssetDTO> updatedAsset = assetService.getAssetsById(assetReturned.getId());

        assertEquals(1, updatedAsset.get().getAssetRecords().size());



    }

}
