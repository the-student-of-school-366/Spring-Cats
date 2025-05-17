package com.example.lab4.repository;

import com.example.lab4.entities.Pet;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
 List<Pet> findPetsByColor(String color);
 Pet findPetById(Long id);
 List<Pet> findByColor(String color, PageRequest pageable);
}
