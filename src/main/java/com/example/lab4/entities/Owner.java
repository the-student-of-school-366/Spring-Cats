package com.example.lab4.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

    @Entity
    @Table(name = "owners")
    public class Owner {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "name")
        private String name;

        @Column(name = "birth_date")
        private LocalDate birthDate;

        @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
        private List<Pet> pets;

        public Owner() {}

        public List<Pet> getPets() {
            return pets;
        }

        public void setPets(List<Pet> pets) {
            this.pets = pets;
        }

        public Owner(String name, LocalDate birthDate) {
            this.name = name;
            this.birthDate = birthDate;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public LocalDate getBirthDate() { return birthDate; }

        public void setName(String name) {
            this.name = name;
        }

        public void setBirthDate(LocalDate date) {
            this.birthDate = date;
        }
    }


