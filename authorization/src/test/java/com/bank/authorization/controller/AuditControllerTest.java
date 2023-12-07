package com.bank.authorization.controller;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.service.AuditService;
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
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class AuditControllerTest {

    @MockBean
    AuditService auditService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        AuditDto expectedResult = getAuditDto();
        Long id = expectedResult.getId();
        when(auditService.findById(id)).thenReturn(expectedResult);

        MvcResult result = mockMvc.perform(get("/audit/" + id))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        AuditDto actualResult = new ObjectMapper().readValue(responseContent, AuditDto.class);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("Чтение по id с неправильным запросом, негативный сценарий")
    void readByIdBadRequestNegativeTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/audit/fake"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(exception -> exception.getResolvedException()
                        .getClass().equals(MethodArgumentTypeMismatchException.class))
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertEquals(response, "Проверьте корректность применяемого запроса.");
    }

    AuditDto getAuditDto() {
        return new AuditDto(1l,
                "entityType",
                "operationType",
                "createdBy",
                "modifiedBy",
                new Timestamp(10L),
                new Timestamp(20L),
                "newEntityJson",
                "entityJson");
    }
}