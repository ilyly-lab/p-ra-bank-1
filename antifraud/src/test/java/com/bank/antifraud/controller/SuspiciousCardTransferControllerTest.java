package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.service.impl.SuspiciousCardTransferServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@FieldDefaults(level = AccessLevel.PRIVATE)
@WebMvcTest(SuspiciousCardTransferController.class)
class SuspiciousCardTransferControllerTest {

    @MockBean
    SuspiciousCardTransferServiceImpl service;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("чтение SuspiciousCardTransfer это подозрительные переводы по номеру карты по id, позитивный сценарий")
    void read() throws Exception {
        Long accountId = 1L;
        SuspiciousCardTransferDto expectedResult = new SuspiciousCardTransferDto(1L, 13L,
                false, false, "blockedReason", "suspiciousReason");

        when(service.findById(accountId)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(get("/suspicious/card/transfer/" + accountId))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        SuspiciousCardTransferDto actualResult = new ObjectMapper().readValue(responseContent,
                SuspiciousCardTransferDto.class);

        assertEquals(expectedResult, actualResult);
        verify(service, times(1)).findById(accountId);
    }

    @Test
    @DisplayName("чтение SuspiciousCardTransfer это подозрительные переводы по номеру карты по списку ids, позитивный сценарий")
    void readAll() throws Exception {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        List<SuspiciousCardTransferDto> expectedResult = new ArrayList<>(Arrays.asList(
                new SuspiciousCardTransferDto(1L, 13L, false, false,
                        "blockedReason", "suspiciousReason"),
                new SuspiciousCardTransferDto(1L, 13L, false, false,
                        "blockedReason", "suspiciousReason"),
                new SuspiciousCardTransferDto(1L, 13L, false, false,
                        "blockedReason", "suspiciousReason")
        ));
        String idsJson = objectMapper.writeValueAsString(ids);

        when(service.findAllById(ids)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(get("/suspicious/card/transfer?ids=1,2,3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(idsJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<SuspiciousCardTransferDto> actualResult = new ObjectMapper().readValue(responseContent,
                new TypeReference<>() {
        });

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("создание SuspiciousCardTransfer это подозрительные переводы по номеру карты, позитивный сценарий")
    void create() throws Exception {
        SuspiciousCardTransferDto expectedResult = new SuspiciousCardTransferDto(1L, 13L,
                false, false, "blockedReason", "suspiciousReason");
        String transferDtoJson = objectMapper.writeValueAsString(expectedResult);

        when(service.save(expectedResult)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(post("/suspicious/card/transfer/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transferDtoJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        SuspiciousCardTransferDto actualResult = new ObjectMapper().readValue(responseContent,
                SuspiciousCardTransferDto.class);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("обновление SuspiciousCardTransfer это подозрительные переводы по номеру карты, позитивный сценарий")
    void update() throws Exception {
        Long id = 1L;
        SuspiciousCardTransferDto expectedResult = new SuspiciousCardTransferDto(1L, 13L,
                false, false, "blockedReason", "suspiciousReason");
        String transferDtoJson = objectMapper.writeValueAsString(expectedResult);

        when(service.update(id, expectedResult)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(put("/suspicious/card/transfer/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transferDtoJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        SuspiciousCardTransferDto actualResult = new ObjectMapper().readValue(responseContent,
                SuspiciousCardTransferDto.class);

        assertEquals(expectedResult, actualResult);
    }
}