package com.bank.transfer.controller;

import com.bank.transfer.dto.CardTransferDto;
import com.bank.transfer.service.CardTransferService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardTransferController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class CardTransferControllerTest {
    @MockBean
    CardTransferService cardTransferService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Получение списка переводов по номеру карты по списку id, позитивный сценарий")
    void readAllCardTransferDtoByListIdPositiveTest() throws Exception {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L));
        List<CardTransferDto> expectedCardTransferDtoList = List.of(getDto1(), getDto2());

        when(cardTransferService.findAllById(ids)).thenReturn(expectedCardTransferDtoList);

        MvcResult result = mockMvc.perform(get("/card/read/all")
                        .param("ids", "1", "2"))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        List<CardTransferDto> resultDtoList = objectMapper.readValue(response,
                new TypeReference<>() {
                });

        assertEquals(expectedCardTransferDtoList, resultDtoList);
        verify(cardTransferService, times(1)).findAllById(ids);
    }

    @Test
    @DisplayName("Получение списка переводов по номеру карты по списку id, негативный сценарий")
    void readAllCardTransferDtoByListIdNegativeTest() throws Exception {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L));

        when(cardTransferService.findAllById(ids)).thenThrow(EntityNotFoundException.class);

        MvcResult result = mockMvc.perform(get("/card/read/all")
                        .param("ids", "1", "2"))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals(404, result.getResponse().getStatus());
        verify(cardTransferService, times(1)).findAllById(ids);
    }

    @Test
    @DisplayName("Получение перевода по номеру карты по id, позитивный сценарий")
    void readCardTransferDtoByIdPositiveTest() throws Exception {
        Long id = 1L;
        CardTransferDto expectedDto = getDto1();

        when(cardTransferService.findById(id)).thenReturn(expectedDto);

        MvcResult result = mockMvc.perform(get("/card/read/{id}", id))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        CardTransferDto resultDto = objectMapper.readValue(response, CardTransferDto.class);

        assertEquals(resultDto, expectedDto);
        verify(cardTransferService, times(1)).findById(id);
    }

    @Test
    @DisplayName("Получение перевода по номеру карты по id, негативный сценарий")
    void readCardTransferDtoByIdNegativeTest() throws Exception {
        Long id = 1L;

        when(cardTransferService.findById(id)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/card/read/{id}", id))
                .andExpect(status().isNotFound()) // Ожидаем статус 404 Not Found
                .andReturn();

        verify(cardTransferService, times(1)).findById(id);
    }

    @Test
    @DisplayName("Создание перевода по номеру карты, позитивный сценарий")
    void createCardTransferDtoPositiveTest() throws Exception {
        CardTransferDto expectedDto = getDto1();
        String expectedDtoJson = objectMapper.writeValueAsString(expectedDto);

        when(cardTransferService.save(expectedDto)).thenReturn(expectedDto);

        MvcResult result = mockMvc.perform(post("/card/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedDtoJson))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        CardTransferDto resultDto = objectMapper.readValue(response, CardTransferDto.class);

        assertEquals(resultDto, expectedDto);
        verify(cardTransferService, times(1)).save(expectedDto);
    }

    @Test
    @DisplayName("Создание перевода по номеру карты, негативный сценарий")
    void createCardTransferDtoNegativeTest() throws Exception {
        CardTransferDto expectedDto = getDto1();
        String expectedDtoJson = objectMapper.writeValueAsString(expectedDto);

        when(cardTransferService.save(expectedDto)).thenThrow(FeignException.InternalServerError.class);

        mockMvc.perform(post("/card/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedDtoJson))
                .andExpect(status().isInternalServerError())
                .andReturn();

        verify(cardTransferService, times(1)).save(expectedDto);
    }

    @Test
    @DisplayName("Обновление перевода по номеру карты по id, позитивный сценарий")
    void updateCardTransferDtoPositiveTest() throws Exception {
        Long id = 1L;
        CardTransferDto expectedDto = getDto1();
        String expectedDtoJson = objectMapper.writeValueAsString(expectedDto);

        when(cardTransferService.update(id, expectedDto)).thenReturn(expectedDto);

        MvcResult result = mockMvc.perform(put("/card/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedDtoJson))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        CardTransferDto resultDto = objectMapper.readValue(response, CardTransferDto.class);

        assertEquals(resultDto, expectedDto);
        verify(cardTransferService, times(1)).update(id, expectedDto);
    }

    @Test
    @DisplayName("Обновление перевода по номеру карты по id, негативный сценарий")
    void updateCardTransferDtoNegativeTest() throws Exception {
        Long id = 1L;
        CardTransferDto expectedDto = getDto1();
        String expectedDtoJson = objectMapper.writeValueAsString(expectedDto);

        when(cardTransferService.update(id, expectedDto)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(put("/card/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(expectedDtoJson))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(cardTransferService, times(1)).update(id, expectedDto);
    }

    private CardTransferDto getDto1() {
        return new CardTransferDto(1L,
                2345L, BigDecimal.valueOf(9999), "asd", 5L);
    }

    private CardTransferDto getDto2() {
        return new CardTransferDto(2L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }
}