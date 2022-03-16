package com.assets.manager.domain.dto;

import com.assets.manager.domain.Asset;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class AssetDTO {
    private Long id;
    private String name;
    private String type;

    public static AssetDTO create(Asset asset){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(asset, AssetDTO.class);
    }

}
