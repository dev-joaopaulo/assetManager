package com.assets.manager.asset_record;

import com.assets.manager.util.UriUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/asset-record")
public class AssetRecordController {

    private final AssetRecordService assetRecordService;

    public AssetRecordController(AssetRecordService assetRecordService) {
        this.assetRecordService = assetRecordService;
    }

    @GetMapping()
    public ResponseEntity getRecordAssets(){
        List<AssetRecordDTO> assetRecords = assetRecordService.getRecords();
        return assetRecords.iterator().hasNext() ?
                ResponseEntity.ok(assetRecords) :
                ResponseEntity.notFound().build();
    }


    @PostMapping()
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity insert(@RequestBody AssetRecordDTO assetRecordDTO){
        AssetRecordDTO insertedAssetRecord = assetRecordService.insert(assetRecordDTO);
        URI location = UriUtil.getUri(insertedAssetRecord.getId());
        return ResponseEntity.created(location).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody  AssetRecordDTO assetRecordDTO){
        AssetRecordDTO updatedAssetRecord = assetRecordService.update(id, assetRecordDTO);
        return updatedAssetRecord != null ?
                ResponseEntity.ok(updatedAssetRecord) :
                ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        assetRecordService.delete(id);
        return ResponseEntity.ok().build();
    }


}
