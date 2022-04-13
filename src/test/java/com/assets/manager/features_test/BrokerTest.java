package com.assets.manager.features_test;

import com.assets.manager.broker.Broker;
import com.assets.manager.broker.BrokerDTO;
import com.assets.manager.broker.BrokerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;

@SpringBootTest
public class BrokerTest {

    @Autowired
    private BrokerService brokerService;

    @Test
    public void insertTest(){

        BrokerDTO brokerDTO = new BrokerDTO();
        brokerDTO.setName("CLEAR");

        BrokerDTO brokerReturned = brokerService.insert(brokerDTO);
        assertNotNull(brokerReturned);

        Long id = brokerReturned.getId();
        assertNotNull(id);

        Broker insertedBroker = brokerService.getBrokerById(brokerReturned.getId());
        assertEquals("CLEAR", insertedBroker.getName());
    }

}
