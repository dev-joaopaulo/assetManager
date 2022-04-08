package com.assets.manager;

import com.assets.manager.asset.Asset;
import com.assets.manager.asset.AssetDTO;
import com.assets.manager.broker.Broker;
import com.assets.manager.asset.AssetService;
import com.assets.manager.broker.BrokerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
@Transactional
class AssetTest {

    @Autowired
    private AssetService assetService;

    @Autowired
    private MockDataService mockDataService;

    @Test
    void insertTest(){

        Broker insertedBroker = mockDataService.insertFakeBroker("CLEAR");

        Asset asset = new Asset();
        asset.setBroker(insertedBroker);
        asset.setName("assetNameTest_");
        asset.setType("assetTypeTest_");

        AssetDTO assetReturned = assetService.insert(asset);

        assertNotNull(assetReturned);

        Long id = assetReturned.getId();
        assertNotNull(id);

        Optional<AssetDTO> optional = assetService.getAssetsById(id);
        assertTrue(optional.isPresent());

        assertEquals("assetNameTest_", optional.get().getName());
        assertEquals("assetTypeTest_", optional.get().getType());

        assetService.delete(id);

        assertFalse(assetService.getAssetsById(id).isPresent());

    }

    @Test
    void testList(){

        Broker insertedBroker = mockDataService.insertFakeBroker("CLEAR");

        Asset asset1 = new Asset();
        asset1.setBroker(insertedBroker);
        asset1.setTicker("assetTickerTest1");
        asset1.setName("assetNameTest1");
        asset1.setType("assetTypeTest1");

        Asset asset2 = new Asset();
        asset2.setBroker(insertedBroker);
        asset2.setTicker("assetTickerTest2");
        asset2.setName("assetNameTest2");
        asset2.setType("assetTypeTest2");

        AssetDTO assetReturned1 = assetService.insert(asset1);
        AssetDTO assetReturned2 = assetService.insert(asset2);

        assertEquals(2,assetService.getAssets(PageRequest.of(0,10)).size());

        assetService.delete(assetReturned1.getId());
        assetService.delete(assetReturned2.getId());

        assertFalse(assetService.getAssetsById(assetReturned1.getId()).isPresent());
        assertFalse(assetService.getAssetsById(assetReturned2.getId()).isPresent());
    }

    @Test
    void insertAssetBroker(){

        Broker insertedBroker = mockDataService.insertFakeBroker("CLEAR");

        Asset asset1 = Asset.builder()
                .assetClass("Fixed")
                .type("Tesouro Direto")
                .broker(insertedBroker)
                .name("NTNB 2031")
                .totalCost(10000.59F)
                .build();

        AssetDTO savedAsset = assetService.insert(asset1);
        Assertions.assertNotNull(savedAsset);
        Assertions.assertEquals(insertedBroker.getId(), savedAsset.getBroker().getId());
        Assertions.assertEquals("Tesouro Direto" ,savedAsset.getType());
    }
}
