package com.example;



import com.example.controller.PetController;
import com.example.dto.PetDto; // Импортируйте ваш PetDto
import com.example.entities.Owner; // Возможно, потребуется для PetDto
import com.example.services.PetService; // Импортируйте ваш PetService
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest для PetController
@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Мокируем PetService
    @MockBean
    private PetService petService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // Пример DTO для использования в тестах
    private PetDto createSamplePetDto(Long id, String name, String breed, String color, LocalDate birthDate, Owner owner) {
        PetDto dto = new PetDto();
        dto.setId(id);
        dto.setName(name);
        dto.setBreed(breed);
        dto.setColor(color);
        dto.setBirthDate(birthDate);
        dto.setOwner(owner); // Внимание: передача всей сущности Owner в DTO может быть не лучшей практикой
        return dto;
    }


    // Тест GET /api/pets/{id} - Успех
    @Test
    void getPet_shouldReturnPetDto_whenPetExists() throws Exception {
        Long petId = 1L;
        PetDto petDto = createSamplePetDto(petId, "Buddy", "Golden Retriever", "Gold", LocalDate.of(2020, 1, 20), null); // Владелец может быть null в DTO для ответа

        when(petService.getPetById(petId)).thenReturn(petDto);

        mockMvc.perform(get("/api/pets/{id}", petId))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(petId))
                .andExpect(jsonPath("$.name").value("Buddy"))
                .andExpect(jsonPath("$.breed").value("Golden Retriever"))
                .andExpect(jsonPath("$.color").value("Gold"))
                .andExpect(jsonPath("$.birthDate").value("2020-01-20"));

        verify(petService, times(1)).getPetById(petId);
    }


    @Test
    void createPet_shouldReturnCreatedPetDto() throws Exception {
        // DTO, которое отправляем в запросе
        PetDto petToCreateDto = createSamplePetDto(null, "New Pet", "Mixed", "White", LocalDate.of(2023, 7, 1), null);
        // DTO, которое вернет сервис после сохранения (с ID)
        PetDto createdPetDto = createSamplePetDto(20L, "New Pet", "Mixed", "White", LocalDate.of(2023, 7, 1), null);

        when(petService.createPet(any(PetDto.class))).thenReturn(createdPetDto);

        mockMvc.perform(post("/api/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petToCreateDto)))
                .andExpect(status().isOk()) // Или .isCreated() (201)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(20L))
                .andExpect(jsonPath("$.name").value("New Pet"))
                .andExpect(jsonPath("$.breed").value("Mixed"))
                .andExpect(jsonPath("$.color").value("White"))
                .andExpect(jsonPath("$.birthDate").value("2023-07-01"));

        verify(petService, times(1)).createPet(any(PetDto.class));
    }

    // Пример теста PUT /api/pets/{id} (обновление питомца)
    @Test
    void updatePet_shouldReturnUpdatedPetDto_whenPetExists() throws Exception {
        Long petId = 5L;
        // DTO с обновленными данными
        PetDto petToUpdateDto = createSamplePetDto(petId, "Updated Name", "Updated Breed", "Updated Color", LocalDate.of(2020, 2, 2), null);
        // DTO, которое вернет сервис
        PetDto updatedPetDto = createSamplePetDto(petId, "Updated Name", "Updated Breed", "Updated Color", LocalDate.of(2020, 2, 2), null);

        when(petService.updatePet(eq(petId), any(PetDto.class))).thenReturn(updatedPetDto);

        mockMvc.perform(put("/api/pets/{id}", petId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petToUpdateDto)))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(petId))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.breed").value("Updated Breed"))
                .andExpect(jsonPath("$.color").value("Updated Color"))
                .andExpect(jsonPath("$.birthDate").value("2020-02-02"));

        verify(petService, times(1)).updatePet(eq(petId), any(PetDto.class));
    }
}