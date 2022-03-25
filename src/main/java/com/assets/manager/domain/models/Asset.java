package com.assets.manager.domain.models;

import lombok.*;

import javax.persistence.*;

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
    private int quantity;
    private float initialValue;
    private float currentValue;
    @ManyToOne
    @JoinColumn(name = "broker_id")
    @JoinTable(name = "asset_broker",
            joinColumns = @JoinColumn(name = "fk_asset"),
            inverseJoinColumns = @JoinColumn(name = "fk_broker"))
    private Broker broker;

}
