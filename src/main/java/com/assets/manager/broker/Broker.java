package com.assets.manager.broker;

import com.assets.manager.asset.Asset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder @AllArgsConstructor @NoArgsConstructor
public class Broker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;

    @OneToMany
    private Set<Asset> assets = new HashSet<>();
}
