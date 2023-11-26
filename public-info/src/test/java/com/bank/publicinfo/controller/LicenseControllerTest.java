package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.service.LicenseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LicenseController.class)
class LicenseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LicenseService service;
    @Autowired
    private ObjectMapper mapper;

    private LicenseDto createTestDto(Long id) {
        var testDto = new LicenseDto();
        testDto.setId(id);
        testDto.setPhotoLicense(new Byte[]{10, 20, 30});
        testDto.setBankDetails(new BankDetailsDto());

        return testDto;
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        Long id = 1L;
        var expectedDto = createTestDto(1L);

        given(service.findById(id)).willReturn(expectedDto);

        ResultActions response = mockMvc.perform(get("/license/{id}", id)
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedDto)));
    }

    @Test
    @DisplayName("чтение по списку ids, позитивный сценарий")
    void readAllByIdsPositiveTest() throws Exception {
        var ids = Arrays.asList(1L, 2L, 3L);
        var expectedDtos = Arrays.asList(
                createTestDto(ids.get(0)),
                createTestDto(ids.get(1)),
                createTestDto(ids.get(2))
        );

        given(service.findAllById(ids)).willReturn(expectedDtos);

        ResultActions response = mockMvc.perform(get("/license/read/all")
                .param("ids", "1", "2", "3")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedDtos)));
    }

    @Test
    @DisplayName("создание записи, позитивный сценарий")
    void createPositiveTest() throws Exception {
        var expectedDto = createTestDto(1L);

        given(service.create(any())).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/license/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(expectedDto)));

        response.andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(expectedDto)));
    }

    @Test
    @DisplayName("обновление записи по id, позитивный сценарий")
    void updatePositiveTest() throws Exception {
        Long id = 1L;
        var requestDto = createTestDto(1L);
        var updatedDto = createTestDto(id);

        given(service.update(id, requestDto)).willReturn(updatedDto);

        ResultActions response = mockMvc.perform(put("/license/update/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto)));

        response.andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(updatedDto)));
    }
}