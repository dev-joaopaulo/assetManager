package com.assets.manager.broker;

import com.assets.manager.asset.Asset;
import org.modelmapper.internal.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrokerService {

    @Autowired
    private BrokerRepository repository;

    public List<Broker> getBrokers(){
        return repository.findAll();
    }

    public Optional<Broker> getBrokerById(Long id){
        return repository.findById(id);
    }

    public Broker insert(Broker broker){
        Assert.isNull(broker.getId(), "It was not possible to insert record");
        return repository.save(broker);
    }

    public void removeAssetFromBroker(Long brokerId, Asset asset){
        Optional<Broker> optionalBroker =  getBrokerById(brokerId);
        if(optionalBroker.isPresent()){
            optionalBroker.get().getAssets().remove(asset);
            repository.save(optionalBroker.get());
        }
    }
}
