package com.assets.manager.features_test;

import com.assets.manager.ManagerApplication;
import com.assets.manager.MockDataService;
import com.assets.manager.asset.Asset;
import com.assets.manager.asset.AssetDTO;
import com.assets.manager.asset.AssetService;
import com.assets.manager.asset_record.AssetRecordDTO;
import com.assets.manager.asset_record.AssetRecordService;
import com.assets.manager.broker.BrokerDTO;
import com.assets.manager.broker.BrokerService;
import com.assets.manager.types.OperationType;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;

import static junit.framework.TestCase.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManagerApplication.class)
@Transactional
public class AssetRecordTest {

    @Autowired
    MockDataService mockDataService;

    @Autowired
    private AssetRecordService assetRecordService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private BrokerService brokerService;

    @Test
    public void insertTest(){

        BrokerDTO brokerDTO = mockDataService.insertFakeBroker("Clear");
        AssetDTO assetDto = mockDataService.insertFakeAsset("testAsset", "testType", brokerDTO.getId());
        AssetRecordDTO assetRecordDTO = mockDataService.insertFakeAssetRecord(assetDto.getId(), 100, 150F, OperationType.BUY);

        Assertions.assertNotNull(assetRecordDTO.getId());
        Assertions.assertEquals(100, assetRecordDTO.getQuantity() );
        Assertions.assertEquals(150.0F, assetRecordDTO.getAverageCostPerShare(), 0.005F);
        Assertions.assertEquals(LocalDate.now(), assetRecordDTO.getOperationDate());

    }

    @Test
    public void insertMultipleRecords(){
        BrokerDTO brokerDTO = brokerService.insert(
                BrokerDTO.builder()
                        .name("CLEAR")
                        .build()
        );
        AssetDTO assetDto = mockDataService.insertFakeAsset("testAsset", "testType", brokerDTO.getId());
        Long assetId = assetDto.getId();
        Assertions.assertNotNull(assetId);

        mockDataService.insertFakeAssetRecord(assetId, 100, 250F, OperationType.BUY);
        assertEquals(250F, new AssetDTO(assetService.getAssetById(assetId)).getAveragePrice());

        mockDataService.insertFakeAssetRecord(assetId, 100, 150F, OperationType.BUY);
        assertEquals(200F, new AssetDTO(assetService.getAssetById(assetId)).getAveragePrice());

        mockDataService.insertFakeAssetRecord(assetId, 50, 100F, OperationType.SELL);
        assertEquals(200F, new AssetDTO(assetService.getAssetById(assetId)).getAveragePrice());

        assetService.delete(assetId);

    }


}
