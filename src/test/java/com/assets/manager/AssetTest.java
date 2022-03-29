package com.assets.manager;

import com.assets.manager.asset.Asset;
import com.assets.manager.broker.Broker;
import com.assets.manager.asset.AssetService;
import com.assets.manager.broker.BrokerService;
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
    private BrokerService brokerService;

    @Test
    void insertTest(){

        Asset asset = new Asset();
        asset.setName("assetNameTest");
        asset.setType("assetTypeTest");

        Asset assetReturned = assetService.insert(asset);

        assertNotNull(assetReturned);

        Long id = assetReturned.getId();
        assertNotNull(id);

        Optional<Asset> optional = assetService.getAssetsById(id);
        assertTrue(optional.isPresent());

        assertEquals("assetNameTest", optional.get().getName());
        assertEquals("assetTypeTest", optional.get().getType());

        assetService.delete(id);

        assertFalse(assetService.getAssetsById(id).isPresent());

    }

    @Test
    void testList(){

        Asset asset1 = new Asset();
        asset1.setName("assetNameTest");
        asset1.setType("assetTypeTest");

        Asset asset2 = new Asset();
        asset2.setName("assetNameTest");
        asset2.setType("assetTypeTest");

        Asset assetReturned1 = assetService.insert(asset1);
        Asset assetReturned2 = assetService.insert(asset2);

        assertEquals(2,assetService.getAssets(PageRequest.of(0,10)).toList().size());

        assetService.delete(assetReturned1.getId());
        assetService.delete(assetReturned2.getId());

        assertEquals(0, assetService.getAssets(PageRequest.of(0,10)).toList().size());
    }

    @Test
    void insertAssetBroker(){

        Broker broker = new Broker();
        broker.setName("CLEAR");

        Broker savedBroker = brokerService.insert(broker);

        Asset asset1 = Asset.builder()
                .assetClass("Fixed")
                .type("Tesouro Direto")
                .broker(savedBroker)
                .name("NTNB 2031")
                .initialValue(10000.59F)
                .build();

        Asset savedAsset = assetService.insert(asset1);
        assertNotNull(savedAsset);
        assertNotNull(savedAsset.getBroker());
        assertNotNull(savedAsset.getType());

        Broker updatedBroker = brokerService.getBrokerById(savedBroker.getId()).get();
        assertTrue(updatedBroker.getAssets().size()>0);
    }
}
