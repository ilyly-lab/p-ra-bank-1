package com.bank.transfer.controller;

import com.bank.transfer.dto.PhoneTransferDto;
import com.bank.transfer.service.PhoneTransferService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhoneTransferController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class PhoneTransferControllerTest {
    @MockBean
    PhoneTransferService phoneTransferService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Получение списка переводов по номеру телефона по списку id, позитивный сценарий")
    void readAllPhoneTransferDtoByListIdPositiveTest() throws Exception {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L));
        List<PhoneTransferDto> expectedPhoneTransferDtoList = List.of(getDto1(), getDto2());

        when(phoneTransferService.findAllById(ids)).thenReturn(expectedPhoneTransferDtoList);

        MvcResult result = mockMvc.perform(get("/phone/read/all")
                        .param("ids", "1", "2"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        List<PhoneTransferDto> resultDtoList = objectMapper.readValue(response,
                new TypeReference<>() {
                });

        assertEquals(expectedPhoneTransferDtoList, resultDtoList);
        verify(phoneTransferService, times(1)).findAllById(ids);
    }

    @Test
    @DisplayName("Получение списка переводов по номеру телефона по списку id, негативный сценарий")
    void readAllPhoneTransferDtoByListIdNegativeTest() throws Exception {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L));

        when(phoneTransferService.findAllById(ids)).thenThrow(EntityNotFoundException.class);

        MvcResult result = mockMvc.perform(get("/phone/read/all")
                        .param("ids", "1", "2"))
                .andExpect(status().isNotFound()) // 404 Error
                .andReturn();

        assertEquals(404, result.getResponse().getStatus());
        verify(phoneTransferService, times(1)).findAllById(ids);
    }

    @Test
    @DisplayName("Получение перевода по номеру телефона по id, позитивный сценарий")
    void readPhoneTransferDtoByIdPositiveTest() throws Exception {
        Long id = 1L;
        PhoneTransferDto expectedDto = getDto1();

        when(phoneTransferService.findById(id)).thenReturn(expectedDto);

        MvcResult result = mockMvc.perform(get("/phone/read/{id}", id))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        PhoneTransferDto resultDto = objectMapper.readValue(response, PhoneTransferDto.class);

        assertEquals(resultDto, expectedDto);
        verify(phoneTransferService, times(1)).findById(id);
    }

    @Test
    @DisplayName("Получение перевода по номеру телефона по id, негативный сценарий")
    void readPhoneTransferDtoByIdNegativeTest() throws Exception {
        Long id = 1L;

        when(phoneTransferService.findById(id)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/phone/read/{id}", id))
                .andExpect(status().isNotFound()) // Ожидаем статус 404 Not Found
                .andReturn();

        verify(phoneTransferService, times(1)).findById(id);
    }

    @Test
    @DisplayName("Создание перевода по номеру телефона, позитивный сценарий")
    void createPhoneTransferDtoPositiveTest() throws Exception {
        PhoneTransferDto expectedDto = getDto1();
        String expectedDtoJson = objectMapper.writeValueAsString(expectedDto);

        when(phoneTransferService.save(expectedDto)).thenReturn(expectedDto);

        MvcResult result = mockMvc.perform(post("/phone/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedDtoJson))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        PhoneTransferDto resultDto = objectMapper.readValue(response, PhoneTransferDto.class);

        assertEquals(resultDto, expectedDto);
        verify(phoneTransferService, times(1)).save(expectedDto);
    }

    @Test
    @DisplayName("Создание перевода по номеру телефона, негативный сценарий")
    void createPhoneTransferDtoNegativeTest() throws Exception {
        PhoneTransferDto expectedDto = getDto1();
        String expectedDtoJson = objectMapper.writeValueAsString(expectedDto);

        when(phoneTransferService.save(expectedDto)).thenThrow(FeignException.InternalServerError.class);

        mockMvc.perform(post("/phone/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedDtoJson))
                .andExpect(status().isInternalServerError()) // Ожидаем статус 500 Internal Server Error
                .andReturn();

        verify(phoneTransferService, times(1)).save(expectedDto);
    }

    @Test
    @DisplayName("Обновление перевода по номеру телефона по id, позитивный сценарий")
    void updatePhoneTransferDtoPositiveTest() throws Exception {
        Long id = 1L;
        PhoneTransferDto expectedDto = getDto1();
        String expectedDtoJson = objectMapper.writeValueAsString(expectedDto);

        when(phoneTransferService.update(id, expectedDto)).thenReturn(expectedDto);

        MvcResult result = mockMvc.perform(put("/phone/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedDtoJson))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        PhoneTransferDto resultDto = objectMapper.readValue(response, PhoneTransferDto.class);

        assertEquals(resultDto, expectedDto);
        verify(phoneTransferService, times(1)).update(id, expectedDto);
    }

    @Test
    @DisplayName("Обновление перевода по номеру телефона по id, негативный сценарий")
    void updatePhoneTransferDtoNegativeTest() throws Exception {
        Long id = 1L;
        PhoneTransferDto expectedDto = getDto1();
        String expectedDtoJson = objectMapper.writeValueAsString(expectedDto);

        when(phoneTransferService.update(id, expectedDto)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/phone/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedDtoJson))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(phoneTransferService, times(1)).update(id, expectedDto);
    }

    private PhoneTransferDto getDto1() {
        return new PhoneTransferDto(1L,
                2345L, BigDecimal.valueOf(9999), "asd", 5L);
    }

    private PhoneTransferDto getDto2() {
        return new PhoneTransferDto(2L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }
}