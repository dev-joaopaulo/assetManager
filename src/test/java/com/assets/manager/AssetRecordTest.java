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
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Optional;

import static junit.framework.TestCase.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManagerApplication.class)
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

        AssetDTO assetDto = mockDataService.insertFakeAsset("testAsset", "testType");
        Assertions.assertNotNull(assetDto);

        AssetRecordDTO assetRecordDTO = mockDataService.insertFakeAssetRecord(assetDto, 100, 150F, OperationType.BUY);

        Assertions.assertNotNull(assetRecordDTO.getId());
        Assertions.assertEquals(100, assetRecordDTO.getQuantity() );
        Assertions.assertEquals(150.0F, assetRecordDTO.getAverageCostPerShare(), 0.005F);
        Assertions.assertEquals(LocalDate.now(), assetRecordDTO.getOperationDate());

        Optional<AssetDTO> updatedAsset = assetService.getAssetsById(assetDto.getId());

        Assertions.assertEquals(1, updatedAsset.get().getAssetRecords().size());
    }

    @Test
    public void insertMultipleRecords(){
        AssetDTO assetDto = mockDataService.insertFakeAsset("testAsset", "testType");
        Long assetId = assetDto.getId();
        Assertions.assertNotNull(assetId);

        mockDataService.insertFakeAssetRecord(assetDto, 100, 250F, OperationType.BUY);
        assertEquals(250F, assetService.getAssetsById(assetId).get().getAveragePrice());

        mockDataService.insertFakeAssetRecord(assetDto, 25, 300F, OperationType.SELL);
        assertEquals(250F, assetService.getAssetsById(assetId).get().getAveragePrice());

        mockDataService.insertFakeAssetRecord(assetDto, 50, 100F, OperationType.SELL);
        assertEquals(250F, assetService.getAssetsById(assetId).get().getAveragePrice());
    }

    @Test
    public void updateTest(){
        AssetDTO assetDto = mockDataService.insertFakeAsset("testAsset", "testType");
        Long assetId = assetDto.getId();
        Assertions.assertNotNull(assetId);

        AssetRecordDTO assetRecordDTO = mockDataService.insertFakeAssetRecord(assetDto, 100, 150F, OperationType.BUY);
        assertNotNull(assetRecordDTO.getId());

        assetRecordDTO.setQuantity(200);
        AssetRecordDTO updatedAssetRecord = assetRecordService.update(assetRecordDTO.getId(), assetRecordDTO);
        assertEquals(assetRecordDTO.getId(), updatedAssetRecord.getId());

        //Check if related asset is also updated
        Optional<AssetDTO> updatedAsset = assetService.getAssetsById(assetId);
        assertEquals(200, updatedAsset.get().getQuantity());

        updatedAssetRecord.setAverageCostPerShare(250);
        assetRecordService.update(assetRecordDTO.getId(), updatedAssetRecord);
        updatedAsset = assetService.getAssetsById(assetId);
        assertEquals(250F, updatedAsset.get().getAveragePrice());


    }

}
