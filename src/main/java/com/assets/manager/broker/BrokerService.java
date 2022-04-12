package com.assets.manager.broker;

import com.assets.manager.asset.Asset;
import com.assets.manager.asset.AssetDTO;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BrokerService {

    @Autowired
    private BrokerRepository brokerRepository;

    public List<Broker> getBrokers(){
        return brokerRepository.findAll();
    }


    public Broker getBrokerById(Long id){
        return brokerRepository.findById(id).orElse(null);
    }


    public BrokerDTO insert(BrokerDTO brokerDTO){
        Assert.isNull(brokerDTO.getId(), "It was not possible to insert record");
        return new BrokerDTO(brokerRepository.save(toEntity(brokerDTO)));
    }


    public void removeAssetFromBroker(Long brokerId, AssetDTO assetDTO){
        Broker broker =  getBrokerById(brokerId);
        if(broker != null){
            broker.getAssets()
                    .removeIf(a -> Objects.equals(a.getId(), assetDTO.getId()));
            brokerRepository.save(broker);
        }
    }


    public void addAssetToBroker(Long brokerId, Asset asset){
        Broker broker =  getBrokerById(brokerId);
        if(broker != null){
            broker.getAssets().add(asset);
            brokerRepository.save(broker);
        }
    }


    public Broker toEntity(BrokerDTO brokerDTO){
        return Broker.builder()
                .id(brokerDTO.getId())
                .name(brokerDTO.getName())
                .assets(new HashSet<>())
                .description(brokerDTO.getDescription())
                .build();
    }


    public Broker toEntity(BrokerDTO brokerDTO, Set<Asset> assets){
        Broker broker = this.toEntity(brokerDTO);
        broker.setAssets(assets);
        return broker;
    }
}
