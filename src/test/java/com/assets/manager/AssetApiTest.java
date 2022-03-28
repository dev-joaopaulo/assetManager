package com.assets.manager;

import com.assets.manager.asset.Asset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ManagerApplication.class, webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssetApiTest extends BaseAPITest{

    private ResponseEntity<Asset> getAsset(String url){
        return get(url, Asset.class);
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

    private ResponseEntity postAsset(){
        Asset asset = new Asset();
        asset.setName("assetNameTest");
        asset.setType("assetTypeTest");
        String url =  "/api/v1/assets";

        return post(url, asset, ResponseEntity.class);
    }

    @Test
    public void testCRUDAsset(){
        ResponseEntity response = postAsset();

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String location = response.getHeaders().get("location").get(0);
        Asset createdAsset = getAsset(location).getBody();

        assertNotNull(createdAsset);
        assertEquals("assetNameTest", createdAsset.getName());
        assertEquals("assetTypeTest", createdAsset.getType());

        createdAsset.setCurrentValue(50.38F);

        put(location, createdAsset, Asset.class);

        Asset updatedAsset = getAsset(location).getBody();

        assertNotNull(updatedAsset);
        assertEquals(50.38F, updatedAsset.getCurrentValue(), 0.0005F);

        delete(location, ResponseEntity.class);

        assertEquals(HttpStatus.NOT_FOUND, getAsset(location).getStatusCode());
    }

    @Test
    public void testGetAssetsOk(){
        ResponseEntity response = getAssets();
        assertEquals(HttpStatus.NOT_FOUND , response.getStatusCode());

        postAsset();
        response = getAssets();
        assertEquals(HttpStatus.OK , response.getStatusCode());
    }
}
