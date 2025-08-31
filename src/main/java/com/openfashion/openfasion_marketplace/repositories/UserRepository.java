package com.openfashion.openfasion_marketplace.repositories;

import com.openfashion.openfasion_marketplace.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findUserById(Long id);

}
