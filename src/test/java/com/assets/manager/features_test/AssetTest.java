package com.assets.manager.features_test;

import com.assets.manager.asset.Asset;
import com.assets.manager.asset.AssetDTO;
import com.assets.manager.asset_record.AssetRecordService;
import com.assets.manager.broker.Broker;
import com.assets.manager.asset.AssetService;
import com.assets.manager.broker.BrokerDTO;
import com.assets.manager.broker.BrokerService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.junit.Assert.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Transactional
class AssetTest {

    @Autowired
    private AssetService assetService;

    @Autowired
    private BrokerService brokerService;

    @Autowired
    private AssetRecordService assetRecordService;

    @Order(1)
    @Test
    void insertTest(){
        BrokerDTO brokerDTO = brokerService.insert(
                BrokerDTO.builder().name("CLEAR").build()
        );

        AssetDTO assetDTO = new AssetDTO();
        assetDTO.setBrokerId(brokerDTO.getId());
        assetDTO.setName("assetNameTest_");
        assetDTO.setType("assetTypeTest_");
        assetDTO.setTicker("assetTickerTest_");

        AssetDTO assetReturned = assetService.insert(assetDTO);

        assertNotNull(assetReturned);

        Long id = assetReturned.getId();
        assertNotNull(id);

        Asset asset1 = assetService.getAssetById(id);

        assertEquals("assetNameTest_", asset1.getName());
        assertEquals("assetTypeTest_", asset1.getType());

        assetService.delete(id);

        assertNull( assetService.getAssetById(id).getId());

    }

    @Order(2)
    @Test
    void testList(){
        BrokerDTO brokerDTO = brokerService.insert(
                BrokerDTO.builder().name("CLEAR").build()
        );

        AssetDTO assetDTO1 = new AssetDTO();
        assetDTO1.setBrokerId(brokerDTO.getId());
        assetDTO1.setTicker("assetTickerTest1");
        assetDTO1.setName("assetNameTest1");
        assetDTO1.setType("assetTypeTest1");

        AssetDTO assetDTO2 = new AssetDTO();
        assetDTO2.setBrokerId(brokerDTO.getId());
        assetDTO2.setTicker("assetTickerTest2");
        assetDTO2.setName("assetNameTest2");
        assetDTO2.setType("assetTypeTest2");

        AssetDTO assetReturned1 = assetService.insert(assetDTO1);
        AssetDTO assetReturned2 = assetService.insert(assetDTO2);

        assertEquals(2,assetService.getAssets(PageRequest.of(0,10)).size());

        assetService.delete(assetReturned1.getId());
        assetService.delete(assetReturned2.getId());

    }

    @Order(3)
    @Test
    void insertAssetBroker(){
        BrokerDTO brokerDTO = brokerService.insert(
                BrokerDTO.builder().name("CLEAR").build()
        );

        AssetDTO assetDTO = AssetDTO.builder()
                .assetClass("Fixed")
                .type("Tesouro Direto")
                .brokerId(brokerDTO.getId())
                .name("NTNB 2031")
                .build();

        AssetDTO savedAsset = assetService.insert(assetDTO);
        Assertions.assertNotNull(savedAsset);
        Assertions.assertEquals(brokerDTO.getId(), savedAsset.getBrokerId());
        Assertions.assertEquals("Tesouro Direto" ,savedAsset.getType());

        assetService.delete(savedAsset.getId());
    }
}
