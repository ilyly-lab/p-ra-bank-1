package com.bank.antifraud.controller;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.service.impl.SuspiciousPhoneTransferServiceImpl;
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
@WebMvcTest(SuspiciousPhoneTransferController.class)
class SuspiciousPhoneTransferControllerTest {


    @MockBean
    SuspiciousPhoneTransferServiceImpl service;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("чтение SuspiciousPhoneTransfer это подозрительные переводы по номеру телефона по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        Long accountId = 1L;
        SuspiciousPhoneTransferDto expectedResult = new SuspiciousPhoneTransferDto(1L, 13L,
                false, false, "blockedReason", "suspiciousReason");

        when(service.findById(accountId)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(get("/suspicious/phone/transfer/" + accountId))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        SuspiciousPhoneTransferDto actualResult = new ObjectMapper().readValue(responseContent,
                SuspiciousPhoneTransferDto.class);

        assertEquals(expectedResult, actualResult);
        verify(service, times(1)).findById(accountId);
    }

    @Test
    @DisplayName("чтение SuspiciousPhoneTransfer это подозрительные переводы по номеру телефона по списку ids, позитивный сценарий")
    void readAll() throws Exception {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        List<SuspiciousPhoneTransferDto> expectedResult = new ArrayList<>(Arrays.asList(
                new SuspiciousPhoneTransferDto(1L, 13L, false, false,
                        "blockedReason", "suspiciousReason"),
                new SuspiciousPhoneTransferDto(1L, 13L, false, false,
                        "blockedReason", "suspiciousReason"),
                new SuspiciousPhoneTransferDto(1L, 13L, false, false,
                        "blockedReason", "suspiciousReason")
        ));
        String idsJson = objectMapper.writeValueAsString(ids);

        when(service.findAllById(ids)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(get("/suspicious/phone/transfer?ids=1,2,3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(idsJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<SuspiciousPhoneTransferDto> actualResult = new ObjectMapper().readValue(responseContent,
                new TypeReference<>() {
        });

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("создание SuspiciousPhoneTransfer это подозрительные переводы по номеру телефона, позитивный сценарий")
    void create() throws Exception {
        SuspiciousPhoneTransferDto expectedResult = new SuspiciousPhoneTransferDto(1L, 13L,
                false, false, "blockedReason", "suspiciousReason");
        String transferDtoJson = objectMapper.writeValueAsString(expectedResult);

        when(service.save(expectedResult)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(post("/suspicious/phone/transfer/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transferDtoJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        SuspiciousPhoneTransferDto actualResult = new ObjectMapper().readValue(responseContent,
                SuspiciousPhoneTransferDto.class);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("обновление SuspiciousPhoneTransfer это подозрительные переводы по номеру телефона, позитивный сценарий")
    void update() throws Exception {
        Long id = 1L;
        SuspiciousPhoneTransferDto expectedResult = new SuspiciousPhoneTransferDto(1L, 13L,
                false, false, "blockedReason", "suspiciousReason");
        String transferDtoJson = objectMapper.writeValueAsString(expectedResult);

        when(service.update(id, expectedResult)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(put("/suspicious/phone/transfer/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transferDtoJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        SuspiciousPhoneTransferDto actualResult = new ObjectMapper().readValue(responseContent,
                SuspiciousPhoneTransferDto.class);

        assertEquals(expectedResult, actualResult);
    }
}