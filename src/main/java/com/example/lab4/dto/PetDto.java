package com.example.lab4.dto;

import com.example.lab4.entities.Owner;

import java.time.LocalDate;

public class PetDto {
    private Long id;
    private String name;
    private String breed;
    private String color;
    private LocalDate birthDate;
    private Owner owner;

    public PetDto() {}

    public PetDto(Long id, String name,  LocalDate birthDate, String breed, String color, Owner owner) {
        this.birthDate = birthDate;
        this.name = name;
        this.id = id;
        this.breed = breed;
        this.color = color;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
