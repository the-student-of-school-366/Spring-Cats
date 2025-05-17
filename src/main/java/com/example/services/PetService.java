package com.example.services;

import com.example.dto.PetDto;
import com.example.entities.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.example.repository.PetRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;
    @Autowired
    public PetService(PetRepository petRepository){
        this.petRepository = petRepository;
    }

    PetDto mapToDto(Pet pet){
        PetDto dto = new PetDto();
        dto.setBirthDate(pet.getBirthDate());
        dto.setName(pet.getName());
        dto.setId(pet.getId());
        dto.setOwner(pet.getOwner());
        dto.setBreed(pet.getBreed());
        dto.setColor(pet.getColor());
        return dto;
    }

    private Pet mapToEntity(PetDto dto, Pet existingEntity) {
        Pet pet = (existingEntity == null) ? new Pet() : existingEntity;
        pet.setName(dto.getName());
        pet.setColor(dto.getColor());
        pet.setOwner(dto.getOwner());
        pet.setBirthDate(dto.getBirthDate());
        pet.setBreed(dto.getBreed());
        return pet;
    }


    public PetDto getPetById(Long id) {
        Pet pet = petRepository.findById(id).orElse(null);
        if (pet == null) {
            return null;
        }
        return mapToDto(pet);
    }

    public List<PetDto> getPets(int page, int size, String color) {
        List<Pet> petEntities = new ArrayList<>();

        if (color == null || color.isEmpty()) {
            petRepository.findAll(PageRequest.of(page, size)).forEach(petEntities::add);
        } else {
            petEntities = petRepository.findByColor(color, PageRequest.of(page, size));
        }

        List<PetDto> result = new ArrayList<>();
        for (Pet pet : petEntities) {
            result.add(mapToDto(pet));
        }
        return result;
    }

    public PetDto createPet(PetDto petDto) {
        Pet pet = mapToEntity(petDto, null);
        Pet saved = petRepository.save(pet);
        return mapToDto(saved);
    }

    public PetDto updatePet(Long id, PetDto petDto) {
        Pet oldPet = petRepository.findById(id).orElse(null);
        if (oldPet == null) {
            return null;
        }
        Pet updated = mapToEntity(petDto, oldPet);
        Pet saved = petRepository.save(updated);
        return mapToDto(saved);
    }

    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }
}
