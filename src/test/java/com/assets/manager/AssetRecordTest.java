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
    private AssetRecordService assetRecordService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private BrokerService brokerService;

    private AssetDTO insertFakeAsset(String name, String type){
        Asset asset = new Asset();
        asset.setName(name);
        asset.setType(type);
        asset.setBroker(insertFakeBroker("Clear"));

        return assetService.insert(asset);
    }

    private Broker insertFakeBroker(String brokerName){
        Broker broker = new Broker();
        broker.setName(brokerName);
        return brokerService.insert(broker);
    }

    private AssetRecordDTO insertFakeAssetRecord(AssetDTO assetDto, int quantity,
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

    @Test
    public void insertTest(){

        AssetDTO assetDto = insertFakeAsset("testAsset", "testType");
        Assertions.assertNotNull(assetDto);

        AssetRecordDTO assetRecordDTO = insertFakeAssetRecord(assetDto, 100, 150F, OperationType.BUY);

        Assertions.assertNotNull(assetRecordDTO.getId());
        Assertions.assertEquals(100, assetRecordDTO.getQuantity() );
        Assertions.assertEquals(150.0F, assetRecordDTO.getAverageCostPerShare(), 0.005F);
        Assertions.assertEquals(LocalDate.now(), assetRecordDTO.getOperationDate());

        Optional<AssetDTO> updatedAsset = assetService.getAssetsById(assetDto.getId());

        Assertions.assertEquals(1, updatedAsset.get().getAssetRecords().size());
    }

    @Test
    public void insertMultipleRecords(){
        AssetDTO assetDto = insertFakeAsset("testAsset", "testType");
        Long assetId = assetDto.getId();
        Assertions.assertNotNull(assetId);

        insertFakeAssetRecord(assetDto, 100, 150F, OperationType.BUY);
        Optional<AssetDTO> updatedAsset = assetService.getAssetsById(assetId);
        Assertions.assertEquals(150F, updatedAsset.get().getAveragePrice());

        insertFakeAssetRecord(assetDto, 100, 250F, OperationType.BUY);
        updatedAsset = assetService.getAssetsById(assetId);
        assertEquals(200F, updatedAsset.get().getAveragePrice());

        insertFakeAssetRecord(assetDto, 25, 300F, OperationType.SELL);
        updatedAsset = assetService.getAssetsById(assetId);
        assertEquals(200F, updatedAsset.get().getAveragePrice());

        insertFakeAssetRecord(assetDto, 50, 100F, OperationType.SELL);
        updatedAsset = assetService.getAssetsById(assetId);
        assertEquals(200F, updatedAsset.get().getAveragePrice());
    }

    @Test
    public void updateTest(){
        AssetDTO assetDto = insertFakeAsset("testAsset", "testType");
        Long assetId = assetDto.getId();
        Assertions.assertNotNull(assetId);

        AssetRecordDTO assetRecordDTO = insertFakeAssetRecord(assetDto, 100, 150F, OperationType.BUY);
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
