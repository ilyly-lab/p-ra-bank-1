package com.bank.publicinfo.controller;

import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.service.BranchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BranchController.class)
class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BranchService service;
    @Autowired
    private ObjectMapper mapper;

    private BranchDto createTestDto(Long id) {
        var testDto = new BranchDto();
        testDto.setId(id);
        testDto.setAddress("BranchAddress" + id);
        testDto.setPhoneNumber(1234567890L);
        testDto.setCity("BranchCity" + id);
        testDto.setStartOfWork(LocalTime.of(8, 0));
        testDto.setEndOfWork(LocalTime.of(18, 0));

        return testDto;
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        Long id = 1L;
        var expectedDto = createTestDto(1L);

        given(service.findById(id)).willReturn(expectedDto);

        ResultActions response = mockMvc.perform(get("/branch/{id}", id)
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

        ResultActions response = mockMvc.perform(get("/branch/read/all")
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

        ResultActions response = mockMvc.perform(post("/branch/create")
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

        ResultActions response = mockMvc.perform(put("/branch/update/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestDto)));

        response.andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(updatedDto)));
    }
}