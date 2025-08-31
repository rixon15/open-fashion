package com.openfashion.openfasion_marketplace.repositories;

import com.openfashion.openfasion_marketplace.models.entities.UserRole;
import com.openfashion.openfasion_marketplace.models.entities.UserRoleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleKey> {


}
