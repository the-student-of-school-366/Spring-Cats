package com.example.lab4.services;

import com.example.lab4.dto.OwnerDto;
import com.example.lab4.entities.Owner;
import com.example.lab4.entities.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lab4.repository.OwnerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService {

    private final  OwnerRepository ownerRepository;
    @Autowired
    public OwnerService( OwnerRepository ownerRepository){
        this.ownerRepository=ownerRepository;
    }


    OwnerDto mapToDto(Owner owner){
        OwnerDto dto = new OwnerDto();
        dto.setBirthDate(owner.getBirthDate());
        dto.setName(owner.getName());
        dto.setId(owner.getId());
        dto.setPets(owner.getPets());
        return dto;
    }

    public OwnerDto getOwnerById(Long id) {
        Owner ownerEntity = ownerRepository.findOwnerById(id);
        if (ownerEntity == null) {
            return null;
        }
        return mapToDto(ownerEntity);
    }

    public List<OwnerDto> getAllOwners() {
        List<Owner> owners = new ArrayList<>();
        ownerRepository.findAll().forEach(owners::add);
        return owners.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public OwnerDto createOwner(OwnerDto ownerDto) {
        Owner owner = new Owner();
        owner.setName(ownerDto.getName());
        owner.setBirthDate(ownerDto.getBirthDate());

        if (ownerDto.getPets() != null && !ownerDto.getPets().isEmpty()) {
            List<Pet> pets = new ArrayList<>();
            for (Pet pet : ownerDto.getPets()) {
                // Создаем новую сущность Pet из данных, пришедших в DTO
                Pet newPet = new Pet();
                newPet.setBreed(pet.getBreed());
                newPet.setName(pet.getName());
                newPet.setColor(pet.getColor());
                newPet.setBirthDate(pet.getBirthDate());

                pet.setOwner(owner);
                pets.add(pet);
            }
            owner.setPets(pets); // Устанавливаем собранный список питомцев для владельца
        }

        Owner saved = ownerRepository.save(owner);
        return mapToDto(saved);
    }

    public OwnerDto updateOwner(Long id, OwnerDto ownerDTO) {
        Owner existingOwner = ownerRepository.findById(id).orElse(null);
        if (existingOwner == null) {
            return null;
        }
        existingOwner.setName(ownerDTO.getName());
        existingOwner.setBirthDate(ownerDTO.getBirthDate());
        Owner saved = ownerRepository.save(existingOwner);
        return mapToDto(saved);
    }

    public void deleteOwner(Long id) {
        ownerRepository.deleteById(id);
    }



}
