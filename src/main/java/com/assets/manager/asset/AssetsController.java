package com.assets.manager.asset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static com.assets.manager.util.UriUtil.getUri;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetsController {

    @Autowired
    AssetService assetService;

    @GetMapping()
    public ResponseEntity<List<AssetDTO>> getAssets(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size){
        List<AssetDTO> assets = assetService.getAssets(PageRequest.of(page, size));
        return assets.iterator().hasNext() ?
                ResponseEntity.ok(assets) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetDTO> getAssets(@PathVariable Long id){
        Optional<AssetDTO> asset = assetService.getAssetsById(id);
        return asset.isPresent() ?
                ResponseEntity.ok(asset.get()) :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/get-assets-by-type/{type}")
    public ResponseEntity<Iterable<AssetDTO>> getAssetsByType(@PathVariable String type){
        List<AssetDTO> assets = assetService.getAssetsByType(type);
        return assets.iterator().hasNext() ?
                ResponseEntity.ok(assets) :
                ResponseEntity.noContent().build();
    }

    @PostMapping()
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity create(@RequestBody AssetDTO assetDTO){
        Asset asset = AssetDTO.reverseMap(assetDTO);
        AssetDTO a = assetService.insert(asset);
        URI location = getUri(a.getId());
        return ResponseEntity.created(location).build();
    }



    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody AssetDTO asset){
        AssetDTO assetUpdated = assetService.update(id, asset);
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
