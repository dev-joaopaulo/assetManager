package com.assets.manager.domain;


import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    Iterable<Asset> findByType(String type);

}
