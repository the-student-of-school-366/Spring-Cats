package dto;

import entities.Pet;

import java.time.LocalDate;
import java.util.List;

public class OwnerDto {

    private Long id;
    private String name;
    private LocalDate birthDate;
    private List<Pet> pets;
    public OwnerDto() {}

    public OwnerDto(Long id, String name,  LocalDate birthDate, List<Pet> pets) {
        this.birthDate = birthDate;
        this.name = name;
        this.id = id;
        this.pets = pets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
