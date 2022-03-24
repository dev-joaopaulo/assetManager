package com.assets.manager;

import com.assets.manager.models.Asset;
import com.assets.manager.services.AssetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
public class AssetTest {

    @Autowired
    private AssetService assetService;

    @Test
    public void insertTest(){

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
    public void testList(){

        Asset asset1 = new Asset();
        asset1.setName("assetNameTest");
        asset1.setType("assetTypeTest");

        Asset asset2 = new Asset();
        asset2.setName("assetNameTest");
        asset2.setType("assetTypeTest");

        Asset assetReturned1 = assetService.insert(asset1);
        Asset assetReturned2 = assetService.insert(asset2);

        assertEquals(2,assetService.getAssets().size());

        assetService.delete(assetReturned1.getId());
        assetService.delete(assetReturned2.getId());

        assertEquals(0, assetService.getAssets().size());


    }
}
