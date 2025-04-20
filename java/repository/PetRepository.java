package repository;

import entities.Pet;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
 List<Pet> findPetsByColor(String color);
 Pet findPetById(Long id);
 List<Pet> findByColor(String color, PageRequest pageable);
}
