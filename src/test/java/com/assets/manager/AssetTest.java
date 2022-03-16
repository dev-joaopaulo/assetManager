package com.assets.manager;

import com.assets.manager.domain.Asset;
import com.assets.manager.domain.AssetService;
import com.assets.manager.domain.dto.AssetDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
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

        AssetDTO assetDTO = assetService.insert(asset);

        assertNotNull(assetDTO);

        Long id = assetDTO.getId();
        assertNotNull(id);

        Optional<AssetDTO> optional = assetService.getAssetsById(id);
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

        AssetDTO assetDTO1 = assetService.insert(asset1);
        AssetDTO assetDTO2 = assetService.insert(asset2);

        assertEquals(2,assetService.getAssets().size());

        assetService.delete(assetDTO1.getId());
        assetService.delete(assetDTO2.getId());

        assertEquals(0, assetService.getAssets().size());


    }
}
