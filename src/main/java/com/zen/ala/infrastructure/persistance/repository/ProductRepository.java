package com.zen.ala.infrastructure.persistance.repository;

import com.zen.ala.infrastructure.persistance.entity.ProductEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** ProductRepository is responsible for data access operations related to ProductEntity. */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {}
