package com.example.lab4.repository;

import com.example.lab4.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    List<Owner> findOwnerByNameIs(String name);

    @Override
    List<Owner> findAll();

    Owner findOwnerById(Long id);
}
