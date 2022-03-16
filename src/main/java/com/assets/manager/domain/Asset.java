package com.assets.manager.domain;

import lombok.Data;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Asset {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String name;

    public Asset() {
    }

    public Asset(String type, String name) {
        this.type = type;
        this.name = name;
    }

}
