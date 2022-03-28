package com.assets.manager.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetsController {

    @Autowired
    AssetService assetService;

    @GetMapping()
    public ResponseEntity<List<Asset>> getAssets(){
        List<Asset> assets = assetService.getAssets();
        return assets.iterator().hasNext() ?
                ResponseEntity.ok(assets) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Asset>> getAssets(@PathVariable Long id){
        Optional<Asset> asset = assetService.getAssetsById(id);
        return asset.isPresent() ?
                ResponseEntity.ok(asset) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/get-assets-by-type/{type}")
    public ResponseEntity<Iterable<Asset>> getAssetsByType(@PathVariable String type){
        Iterable<Asset> assets = assetService.getAssetsByType(type);
        return assets.iterator().hasNext() ?
                ResponseEntity.ok(assets) :
                ResponseEntity.noContent().build();
    }

    @PostMapping()
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity create(@RequestBody Asset asset){
            Asset a = assetService.insert(asset);
            URI location = getUri(asset.getId());
            return ResponseEntity.created(location).build();
    }

    private URI getUri(Long id){
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody Asset asset){
        Asset assetUpdated = assetService.update(id, asset);
        return assetUpdated != null ?
                ResponseEntity.ok(assetUpdated) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id){
        assetService.delete(id);
        return ResponseEntity.ok().build();
    }
}
