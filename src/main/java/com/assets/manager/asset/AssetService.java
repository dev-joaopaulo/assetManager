package com.assets.manager.asset;

import com.assets.manager.asset_record.AssetRecord;
import com.assets.manager.asset_record.AssetRecordDTO;
import com.assets.manager.broker.Broker;
import com.assets.manager.broker.BrokerRepository;
import com.assets.manager.types.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private BrokerRepository brokerRepository;

    public List<AssetDTO> getAssets(Pageable pageable){
        return assetRepository.
                findAll(pageable).
                stream().
                map(AssetDTO::create).collect(Collectors.toList());
    }

    public Optional<AssetDTO> getAssetsById(Long id) {

        return assetRepository.findById(id).map(AssetDTO::create);
    }

    public List<AssetDTO> getAssetsByType(String type) {
        return assetRepository.findByType(type)
                .stream()
                .map(AssetDTO::create)
                .collect(Collectors.toList());
    }

    public AssetDTO insert(Asset asset) {
        Assert.isNull(asset.getId(), "It was not possible to insert record: null Asset");
        Assert.isTrue(!isExistingAsset(asset), "It was not possible to insert record: duplicated Asset");
        Assert.notNull(asset.getBroker(), "Forbidden - Asset does not have a Broker");

        Asset savedAsset = assetRepository.save(asset);

        Optional<Broker> optionalBroker = brokerRepository.findById(savedAsset.getBroker().getId());
        optionalBroker.ifPresent(broker -> broker.getAssets().add(savedAsset));
        brokerRepository.save(optionalBroker.get());

        return AssetDTO.create(savedAsset);
    }

    public AssetDTO update(Long id, AssetDTO asset) {
        Assert.notNull(id, "Not possible to update asset entry");

        Optional<AssetDTO> optionalAsset = getAssetsById(id);
        if(optionalAsset.isPresent()){
            AssetDTO dbAsset = optionalAsset.get();
            dbAsset.setName(asset.getName());
            dbAsset.setAssetRecords(updateAssetRecordList(dbAsset.getAssetRecords(), asset.getAssetRecords()));
            dbAsset.updateQuantityAndCost();

            return AssetDTO.create(assetRepository.save(AssetDTO.reverseMap(dbAsset)));
        } else{
            throw new RuntimeException("Not possible to update asset entry");
        }
    }

    public AssetDTO updateAssetRecord(AssetRecordDTO assetRecordDTO) {
        Long assetId = assetRecordDTO.getAsset().getId();
        Optional<AssetDTO> optionalAssetDTO = getAssetsById(assetId);
        if (optionalAssetDTO.isPresent()) {
            AssetDTO assetDTO = optionalAssetDTO.get();
            Set<AssetRecord> assetRecordsList = assetDTO.getAssetRecords();
            for (AssetRecord assetRecord : assetRecordsList) {
                if (Objects.equals(assetRecord.getId(), assetRecordDTO.getId())) {
                    assetRecord = AssetRecordDTO.reverseMap(assetRecordDTO);
                    assetDTO.updateQuantityAndCost();
                }
            }
            return AssetDTO.create(
                    assetRepository.save(AssetDTO.reverseMap(optionalAssetDTO.get()))
            );
        } else {
            throw new RuntimeException("Not possible to update asset entry");
        }
    }

    public void delete(Long id) {

        Asset asset = assetRepository.getById(id);
        Assert.notNull(asset.getBroker(), "Forbidden - Asset does not have a Broker");
        asset.getBroker().getAssets().remove(asset);
        assetRepository.deleteById(id);
    }

    private boolean isExistingAsset(Asset asset){
       List<Asset> existingAssets =  assetRepository.findByTicker(asset.getTicker());
       if(existingAssets.iterator().hasNext()){
           for (Asset a : existingAssets) {
               if(Objects.equals(asset.getBroker().getId(), a.getBroker().getId())){
                   return true;
               }
           }
       }
       return false;
    }

    private Set<AssetRecord> updateAssetRecordList(Set<AssetRecord> existingList, Set<AssetRecord> updatedList){
        return new HashSet<>(Stream.concat(existingList.stream(), updatedList.stream()).collect(Collectors.toList()));
    }


}
