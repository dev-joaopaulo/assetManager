package com.assets.manager.asset_record;

import com.assets.manager.asset.AssetDTO;
import com.assets.manager.asset.AssetService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssetRecordService {

    @Autowired
    private AssetRecordRepository assetRecordRepository;

    @Autowired
    private AssetService assetService;

    public List<AssetRecordDTO> getRecords(){
        return assetRecordRepository.findAll()
                .stream()
                .map(AssetRecordDTO::create)
                .collect(Collectors.toList());
    }

    public Optional<AssetRecordDTO> getById(Long id){
        return assetRecordRepository.findById(id).map(AssetRecordDTO::create);
    }

    public AssetRecordDTO insert(AssetRecordDTO assetRecordDTO){

        Assert.notNull(assetRecordDTO.getAsset().getId(), "It was not possible to insert asset record");

        assetRecordDTO.setOperationDate(LocalDate.now());
        AssetRecord insertedRecord = assetRecordRepository.save(AssetRecordDTO.reverseMap(assetRecordDTO));
        Long assetId = insertedRecord.getAsset().getId();

        Optional<AssetDTO> optionalAssetDTO = assetService.getAssetsById(assetId);

        if(optionalAssetDTO.isPresent()){
            AssetDTO asset = optionalAssetDTO.get();
            asset.getAssetRecords().add(insertedRecord);
            assetService.update(assetId, asset);
        }

        return AssetRecordDTO.create(insertedRecord);
    }

    public void delete(Long id){
        assetRecordRepository.deleteById(id);
    }

    public AssetRecordDTO update(Long id, AssetRecordDTO assetRecordDTO){
        Assert.notNull(id, "Not possible to update object: null id asset record");

        Optional<AssetRecord> optionalAssetRecord = assetRecordRepository.findById(id);
        if(optionalAssetRecord.isPresent()){
            AssetRecord dbAssetRecord = optionalAssetRecord.get();
            dbAssetRecord.setAsset(assetRecordDTO.getAsset());
            dbAssetRecord.setOperationDate(assetRecordDTO.getOperationDate());
            dbAssetRecord.setQuantity(assetRecordDTO.getQuantity());
            dbAssetRecord.setAverageCostPerShare(dbAssetRecord.getAverageCostPerShare());
            dbAssetRecord.setOperationType(assetRecordDTO.getOperationType());

            return AssetRecordDTO.create(assetRecordRepository.save(dbAssetRecord));
        }
        else {
            throw new RuntimeException("Not possible to update Asset Record");
        }
    }
}
