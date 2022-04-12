package com.assets.manager.broker;

import com.assets.manager.asset.Asset;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
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
