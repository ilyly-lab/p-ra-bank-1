package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.service.AuditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.sql.Timestamp;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(controllers = AuditController.class)
class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuditService service;
    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("чтение по id, позитвный сценарий")
    void readPositiveTest() throws Exception {
        Long id = 1L;
        var expectedDto = new AuditDto();
        expectedDto.setId(1L);
        expectedDto.setEntityType("Type");
        expectedDto.setOperationType("OperationType");
        expectedDto.setCreatedBy("CreatedBy");
        expectedDto.setModifiedBy("ModifiedBy");
        expectedDto.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        expectedDto.setModifiedAt(new Timestamp(System.currentTimeMillis() + 10));
        expectedDto.setNewEntityJson("NewEntityJson");
        expectedDto.setEntityJson("EntityJson");

        given(service.findById(id)).willReturn(expectedDto);

        ResultActions result = mockMvc.perform(get("/audit/{id}", id)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedDto)));
    }
}
