package com.assets.manager.asset;

import com.assets.manager.asset_record.AssetRecord;
import com.assets.manager.broker.Broker;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class Asset {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assetClass;
    private String type;
    private String name;
    private String ticker;
    private int quantity;
    private float averagePrice;
    private float currentPrice;

    @ManyToOne
    @JoinColumn(name = "broker_id")
    @JoinTable(name = "asset_broker",
            joinColumns = @JoinColumn(name = "fk_asset"),
            inverseJoinColumns = @JoinColumn(name = "fk_broker"))
    private Broker broker;

    @OneToMany
    private Set<AssetRecord> assetRecords = new HashSet<>();

}
