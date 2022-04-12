package com.assets.manager.asset;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByType(String type);

    List<Asset> findByTicker(String ticker);

}
