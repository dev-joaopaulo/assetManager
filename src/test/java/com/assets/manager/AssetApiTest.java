package com.assets.manager;

import com.assets.manager.domain.models.Asset;
import com.assets.manager.services.AssetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssetApiTest {

    @Autowired
    protected TestRestTemplate rest;

    @Autowired
    private AssetService service;

    private ResponseEntity<Asset> getAsset(String url){
        return
                rest.withBasicAuth("admin", "admin").getForEntity(url, Asset.class);
    }

    @Test
    public void testGetAssetOk(){
        ResponseEntity response = getAsset("/api/v1/assets/get-assets");
        assertEquals(HttpStatus.OK , response.getStatusCode());
    }
}
