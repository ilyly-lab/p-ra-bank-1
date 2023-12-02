package com.bank.transfer.controller;

import com.bank.transfer.dto.AuditDto;
import com.bank.transfer.service.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class AuditControllerTest {
    @MockBean
    AuditService auditService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Получение информации об изменениях переводов по id, позитивный сценарий")
    void readAuditDtoPositiveTest() throws Exception {
        AuditDto expectedDto = getDto();
        Long id = 1L;

        when(auditService.findById(id)).thenReturn(expectedDto);

        MvcResult result = mockMvc.perform(get("/audit/{id}", id))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        AuditDto actualDto = objectMapper.readValue(response, AuditDto.class);

        Assertions.assertThat(actualDto.getId()).isEqualTo(expectedDto.getId());
        verify(auditService, times(1)).findById(id);
    }

    @Test
    @DisplayName("Получение информации об изменениях переводов по id, негативный сценарий")
    void readAuditDtoNegativeTest() throws Exception {
        Long id = 1L;

        when(auditService.findById(id)).thenThrow(EntityNotFoundException.class);

        MvcResult result = mockMvc.perform(get("/audit/{id}", id))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals(404, result.getResponse().getStatus());
        verify(auditService, times(1)).findById(id);
    }

    private AuditDto getDto() {
        return new AuditDto(1L,
                "typeEntity1", "operationType1",
                "createdType1", "modifiedBy", new Timestamp(23),
                new Timestamp(11), "newEntityJson", "EntityJson");
    }
}