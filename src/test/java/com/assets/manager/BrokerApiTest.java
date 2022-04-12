package com.assets.manager;

import com.assets.manager.broker.BrokerDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class BrokerApiTest extends BaseAPITest{

    @Test
    public void createTest(){
        String postUrl = "/api/v1/broker";

        BrokerDTO brokerDTO = BrokerDTO.builder()
                .name("Clear")
                .build();

        ResponseEntity response = post(postUrl, brokerDTO, ResponseEntity.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

    }

}
