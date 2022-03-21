package com.assets.manager.repositories;


import com.assets.manager.models.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    Iterable<Asset> findByType(String type);

}
