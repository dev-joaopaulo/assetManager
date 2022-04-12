package com.assets.manager.asset_record;

import com.assets.manager.asset.Asset;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class AssetRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "record_id")
    @JoinTable(name = "record_asset",
            joinColumns = @JoinColumn(name = "fk_record"),
            inverseJoinColumns = @JoinColumn(name = "fk_asset"))
    private Asset asset;
    private float averageCostPerShare;
    private int quantity;
    private String operationType; // buy or sell
    private LocalDate operationDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssetRecord that = (AssetRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
