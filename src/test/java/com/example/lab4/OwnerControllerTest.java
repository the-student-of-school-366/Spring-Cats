package com.example.lab4;


import com.example.lab4.controller.OwnerController;
import com.example.lab4.dto.OwnerDto; // Импортируйте ваш OwnerDto
import com.example.lab4.services.OwnerService; // Импортируйте ваш OwnerService
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; // Используем @WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean; // Для мокирования сервиса
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// @WebMvcTest загружает только контекст, связанный с веб-слоем, и автоконфигурирует MockMvc
// Указываем контроллер(ы), которые хотим тестировать
@WebMvcTest(OwnerController.class)
class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc; // MockMvc уже сконфигурирован благодаря @WebMvcTest

    // Мокируем сервис, так как @WebMvcTest не загружает Service-слой
    @MockBean
    private OwnerService ownerService;

    private ObjectMapper objectMapper; // Для работы с JSON

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // Тест GET /api/owners/{id} - Успех
    @Test
    void getOwner_shouldReturnOwnerDto_whenOwnerExists() throws Exception {
        Long ownerId = 1L;
        OwnerDto ownerDto = new OwnerDto(ownerId, "Test Owner", LocalDate.of(1990, 5, 15), Collections.emptyList());

        when(ownerService.getOwnerById(ownerId)).thenReturn(ownerDto);

        mockMvc.perform(get("/api/owners/{id}", ownerId))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ownerId))
                .andExpect(jsonPath("$.name").value("Test Owner"))
                .andExpect(jsonPath("$.birthDate").value("1990-05-15"));

        verify(ownerService, times(1)).getOwnerById(ownerId);
    }

    // Тест GET /api/owners/{id} - Не найден

    // Тест GET /api/owners - Список не пуст
    @Test
    void getAllOwners_shouldReturnListOfOwnerDtos() throws Exception {
        OwnerDto owner1 = new OwnerDto(1L, "Owner 1", LocalDate.of(1985, 1, 1), Collections.emptyList());
        OwnerDto owner2 = new OwnerDto(2L, "Owner 2", LocalDate.of(1995, 2, 2), Collections.emptyList());
        List<OwnerDto> ownerList = Arrays.asList(owner1, owner2);

        when(ownerService.getAllOwners()).thenReturn(ownerList);

        mockMvc.perform(get("/api/owners"))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Owner 1"))
                .andExpect(jsonPath("$[1].name").value("Owner 2"));

        verify(ownerService, times(1)).getAllOwners();
    }

    // Тест GET /api/owners - Список пуст
    @Test
    void getAllOwners_shouldReturnEmptyList_whenNoOwnersExist() throws Exception {
        List<OwnerDto> ownerList = Collections.emptyList();

        when(ownerService.getAllOwners()).thenReturn(ownerList);

        mockMvc.perform(get("/api/owners"))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(content().json("[]")); // Проверка на пустой JSON массив

        verify(ownerService, times(1)).getAllOwners();
    }

    // Тест POST /api/owners - Успех
    @Test
    void createOwner_shouldReturnCreatedOwnerDto() throws Exception {
        OwnerDto ownerToCreateDto = new OwnerDto(null, "New Owner", LocalDate.of(2000, 10, 20), Collections.emptyList());
        // Сервис вернет DTO с присвоенным ID
        OwnerDto createdOwnerDto = new OwnerDto(10L, "New Owner", LocalDate.of(2000, 10, 20), Collections.emptyList());

        // Важно: when должен сработать для любого OwnerDto, который приходит в сервис
        when(ownerService.createOwner(any(OwnerDto.class))).thenReturn(createdOwnerDto);

        mockMvc.perform(post("/api/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        // Преобразуем DTO в JSON строку для тела запроса
                        .content(objectMapper.writeValueAsString(ownerToCreateDto)))
                .andExpect(status().isOk()) // Или .isCreated() если контроллер возвращает 201
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.name").value("New Owner"))
                .andExpect(jsonPath("$.birthDate").value("2000-10-20"));

        // Проверяем, что метод сервиса был вызван 1 раз с любым OwnerDto
        verify(ownerService, times(1)).createOwner(any(OwnerDto.class));
        // Если нужно проверить, что в сервис пришел DTO с правильными полями (кроме ID),
        // можно использовать ArgumentCaptor или более сложный ArgumentMatcher.
        // Например: verify(ownerService, times(1)).createOwner(argThat(dto ->
        //    dto.getName().equals("New Owner") && dto.getBirthDate().equals(LocalDate.of(2000, 10, 20))));
    }

    // Тест PUT /api/owners/{id} - Успех
    @Test
    void updateOwner_shouldReturnUpdatedOwnerDto_whenOwnerExists() throws Exception {
        Long ownerId = 1L;
        // DTO с обновленными данными, которое отправляем
        OwnerDto ownerToUpdateDto = new OwnerDto(ownerId, "Updated Owner", LocalDate.of(1991, 6, 16), Collections.emptyList());
        // DTO, которое вернет сервис после обновления
        OwnerDto updatedOwnerDto = new OwnerDto(ownerId, "Updated Owner", LocalDate.of(1991, 6, 16), Collections.emptyList());

        // Когда вызывается updateOwner с этим ID и любым OwnerDto
        when(ownerService.updateOwner(eq(ownerId), any(OwnerDto.class))).thenReturn(updatedOwnerDto);

        mockMvc.perform(put("/api/owners/{id}", ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerToUpdateDto)))
                .andExpect(status().isOk()) // 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(ownerId))
                .andExpect(jsonPath("$.name").value("Updated Owner"))
                .andExpect(jsonPath("$.birthDate").value("1991-06-16"));

        // Проверяем вызов сервиса с правильным ID и любым OwnerDto
        verify(ownerService, times(1)).updateOwner(eq(ownerId), any(OwnerDto.class));
    }

    // Тест PUT /api/owners/{id} - Не найден

    // Тест DELETE /api/owners/{id} - Успех
    @Test
    void deleteOwner_shouldReturnNoContent_whenOwnerExists() throws Exception {
        Long ownerId = 1L;

        // Указываем моку сервиса ничего не делать при вызове deleteOwner
        doNothing().when(ownerService).deleteOwner(ownerId);
        // Если сервис выбрасывает исключение при отсутствии, нужно мокировать этот случай
        // doThrow(new ResourceNotFoundException()).when(ownerService).deleteOwner(ownerId);

        mockMvc.perform(delete("/api/owners/{id}", ownerId))
                .andExpect(status().isOk()); // Или .isNoContent() (204) если контроллер возвращает 204

        // Проверяем, что метод сервиса был вызван
        verify(ownerService, times(1)).deleteOwner(ownerId);
    }

    // Если deleteOwner в сервисе возвращает булево или выбрасывает исключение
    // при отсутствии, нужно добавить тест на Not Found, аналогично GET/PUT.
    // Например, если сервис выбрасывает исключение при отсутствии:
    /*
    @Test
    void deleteOwner_shouldReturnNotFound_whenOwnerDoesNotExist() throws Exception {
         Long ownerId = 99L;
         doThrow(new ResourceNotFoundException("Owner not found")).when(ownerService).deleteOwner(ownerId);

         mockMvc.perform(delete("/api/owners/{id}", ownerId))
                 .andExpect(status().isNotFound());

         verify(ownerService, times(1)).deleteOwner(ownerId);
    }
    */
}