package com.assets.manager.asset_record;

import com.assets.manager.util.UriUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController("/api/v1/assets-records")
public class AssetRecordController {

    @Autowired
    private AssetRecordService assetRecordService;

    @GetMapping()
    public ResponseEntity getRecordAssets(){
        List<AssetRecordDTO> assetRecords = assetRecordService.getRecords();
        return assetRecords.iterator().hasNext() ?
                ResponseEntity.ok(assetRecords) :
                ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity insert(AssetRecordDTO assetRecordDTO){
        AssetRecordDTO a = assetRecordService.insert(assetRecordDTO);
        URI location = UriUtil.getUri(a.getId());
        return ResponseEntity.created(location).build();
    }
}
