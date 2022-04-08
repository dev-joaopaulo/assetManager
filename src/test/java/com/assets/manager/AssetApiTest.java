package com.assets.manager;

import com.assets.manager.asset.Asset;
import com.assets.manager.asset.AssetDTO;
import com.assets.manager.asset.AssetService;
import com.assets.manager.broker.Broker;
import com.assets.manager.broker.BrokerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManagerApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssetApiTest extends BaseAPITest{

    @Autowired
    private BrokerService brokerService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private MockDataService mockDataService;

    @Test
    public void testCreateAsset(){
        ResponseEntity response = postFakeAsset("assetNameTest",
                "assetTickerTest", "assetTypeTest");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String location = response.getHeaders().get("location").get(0);
        AssetDTO createdAsset = getAsset(location).getBody();

        assertNotNull(createdAsset);
        assertEquals("assetNameTest", createdAsset.getName());
        assertEquals("assetTypeTest", createdAsset.getType());

        delete(location, ResponseEntity.class);
        assertEquals(HttpStatus.NOT_FOUND, getAsset(location).getStatusCode());
    }

    @Test
    public void testUpdateAsset(){
        ResponseEntity response = postFakeAsset("assetNameTest",
                "assetTickerTest", "assetTypeTest");

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String location = response.getHeaders().get("location").get(0);
        AssetDTO createdAsset = getAsset(location).getBody();

        createdAsset.setName("updatedAssetName");

        put(location, createdAsset, Asset.class);

        AssetDTO updatedAsset = getAsset(location).getBody();

        assertNotNull(updatedAsset);
        assertEquals("updatedAssetName",updatedAsset.getName());

        delete(location, ResponseEntity.class);
        assertEquals(HttpStatus.NOT_FOUND, getAsset(location).getStatusCode());
    }

    @Test
    public void testDeleteAsset(){
        ResponseEntity response = postFakeAsset("assetNameTest",
                "assetTickerTest", "assetTypeTest");

        String location = response.getHeaders().get("location").get(0);
        AssetDTO createdAsset = getAsset(location).getBody();

        assertNotNull(createdAsset);

        delete(location, ResponseEntity.class);
        assertEquals(HttpStatus.NOT_FOUND, getAsset(location).getStatusCode());
    }

    @Test
    public void testReadAssets(){

        Broker broker = mockDataService.insertFakeBroker("CLEAR");
        AssetDTO assetDTO = mockDataService.insertFakeAsset("fakeReadAssetName","fakeReadAssetType", broker);

        ResponseEntity response = getAssets();
        assertEquals(HttpStatus.OK , response.getStatusCode());

        assetService.delete(assetDTO.getId());
    }

    private ResponseEntity<AssetDTO> getAsset(String url){
        return get(url, AssetDTO.class);
    }

    private ResponseEntity<List<Asset>> getAssets(){
        String url = "/api/v1/assets/";
        HttpHeaders headers = getHeaders();
        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Asset>>() {
                });
    }

    private ResponseEntity postFakeAsset(String assetName, String assetTicker, String assetType){

        Broker broker = mockDataService.insertFakeBroker("CLEAR");

        Asset asset = new Asset();
        asset.setName(assetName);
        asset.setTicker(assetTicker);
        asset.setType(assetType);
        asset.setBroker(broker);

        String url =  "/api/v1/assets";
        return post(url, AssetDTO.create(asset), ResponseEntity.class);
    }
}
