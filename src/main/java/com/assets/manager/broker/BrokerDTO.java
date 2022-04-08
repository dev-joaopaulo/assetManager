package com.assets.manager.broker;

import com.assets.manager.asset.Asset;
import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class BrokerDTO {

    private Long id;
    private String name;
    private String description;
    private List<Long> assetIds = new ArrayList<>();

    public BrokerDTO(Broker broker){
        this.id = broker.getId();
        this.name = broker.getName();
        this.description = broker.getDescription();
        this.assetIds = getAssetIds(broker.getAssets());
    }

    public Broker toEntity(){
        return Broker.builder()
                .id(this.getId())
                .name(this.getName())
                .assets(new HashSet<>())
                .description(this.description)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BrokerDTO broker = (BrokerDTO) o;
        return Objects.equals(id, broker.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private List<Long> getAssetIds(Set<Asset> assets){
        return assets
                .stream()
                .map(Asset::getId)
                .collect(Collectors.toList());
    }
}
