package com.example.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "breed")
    private String breed;
    @Column(name = "color")
    private String color;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;


    @ManyToMany
    @JoinTable(
            name = "pet_friends",
            joinColumns = @JoinColumn(name = "pet_id1"),
            inverseJoinColumns = @JoinColumn(name = "pet_id2")
    )
    private List<Pet> friends;

    public Pet() {
    }

    public Pet(String name, String breed, String color, Owner owner, LocalDate birthDate) {
        this.name = name;
        this.breed = breed;
        this.color = color;
        this.owner = owner;
        this.birthDate = birthDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getColor() {
        return color;
    }

    public Owner getOwner() {
        return owner;
    }

    public List<Pet> getFriends() {
        return friends;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(LocalDate date) {
        this.birthDate = date;

    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}