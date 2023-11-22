package com.bank.account.controller;

import com.bank.account.dto.AuditDto;
import com.bank.account.service.AccountAuditServiceImpl;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(AccountAuditController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class AccountAuditControllerTest {

    @MockBean
    AccountAuditServiceImpl accountAuditService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        Long accountId = 1L;
        AuditDto expectedResult = new AuditDto(1L, "entityType", "operationType",
                "createdBy", "modifiedBy", new Timestamp(10L), new Timestamp(20L),
                "newEntityJson", "entityJson");

        when(accountAuditService.findById(accountId)).thenReturn(expectedResult);

        MvcResult result = mockMvc.perform(get("/audit/" + accountId))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        AuditDto actualResult = new ObjectMapper().readValue(responseContent, AuditDto.class);

        assertEquals(expectedResult, actualResult);
    }
}