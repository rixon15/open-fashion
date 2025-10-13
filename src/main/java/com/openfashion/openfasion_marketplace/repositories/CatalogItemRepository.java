package com.openfashion.openfasion_marketplace.repositories;

import com.openfashion.openfasion_marketplace.models.entities.CatalogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CatalogItemRepository extends JpaRepository<CatalogItem, Long> {


}
