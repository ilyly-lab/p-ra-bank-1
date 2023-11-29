package com.bank.antifraud.controller;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.service.impl.AuditServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class AuditControllerTest {

    @MockBean
    AuditServiceImpl auditService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        AuditDto expectedResult = new AuditDto(1L, "entityType", "operationType",
                "createdBy", "modifiedBy", new Timestamp(10L), new Timestamp(20L),
                "newEntityJson", "entityJson");

        when(auditService.findById(1L)).thenReturn(expectedResult);

        MvcResult result = mockMvc.perform(get("/audit/" + 1))
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        AuditDto actualResult = new ObjectMapper().readValue(responseContent, AuditDto.class);

        assertEquals(expectedResult, actualResult);
    }

}