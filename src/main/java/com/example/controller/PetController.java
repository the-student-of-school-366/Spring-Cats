package com.example.controller;

import com.example.dto.PetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.services.PetService;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    private final PetService petService;
    @Autowired
    public PetController(PetService petService){
        this.petService=petService;
    }

    @GetMapping("/{id}")
    public PetDto getPet(@PathVariable Long id) {
        return petService.getPetById(id);
    }

    @GetMapping
    public List<PetDto> getPets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String color
    ) {
        return petService.getPets(page, size, color);
    }

    @PostMapping
    public PetDto createPet(@RequestBody PetDto petDTO) {
        return petService.createPet(petDTO);
    }

    @PutMapping("/{id}")
    public PetDto updatePet(@PathVariable Long id, @RequestBody PetDto petDTO) {
        return petService.updatePet(id, petDTO);
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id) {
        petService.deletePet(id);
    }
}
