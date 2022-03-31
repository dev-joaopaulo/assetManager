package com.assets.manager.asset_record;

import com.assets.manager.asset.Asset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AssetRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "record_id")
    @JoinTable(name = "record_asset",
            joinColumns = @JoinColumn(name = "fk_record"),
            inverseJoinColumns = @JoinColumn(name = "fk_asset"))
    private Asset asset;
    private float averageCostPerShare;
    private int quantity;
    private String operationType; // buy or sell

}
