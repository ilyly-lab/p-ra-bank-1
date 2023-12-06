package com.bank.history.controller;

import com.bank.history.dto.HistoryDto;
import com.bank.history.service.HistoryServiceImpl;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HistoryController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class HistoryControllerTest {

    @MockBean
    HistoryServiceImpl historyService;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void read() throws Exception {
        Long accountId = 1L;

        HistoryDto expectedResult = getHistoryDto();

        when(historyService.readById(accountId)).thenReturn(expectedResult);

        MvcResult result = mockMvc.perform(get("/api/history/" + accountId))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        HistoryDto actualResult = new ObjectMapper().readValue(responseContent, HistoryDto.class);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("чтение по списку id, позитивный сценарий")
    void readAll() throws Exception {
        List<Long> ids = new ArrayList<>(Arrays.asList(111L, 222L, 333L));
        List<HistoryDto> expectedResult = getListDTO();

        when(historyService.readAllById(ids)).thenReturn(expectedResult);

        MvcResult result = mockMvc.perform(get("/api/history")
                        .param("id", "111", "222", "333")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        List<HistoryDto> actualResult = objectMapper.readValue(responseContent,
                new TypeReference<>() {
                }
        );

        assertEquals(expectedResult, actualResult);
        verify(historyService, times(1)).readAllById(ids);
    }

    @Test
    @DisplayName("создание сущности HistoryEntity, позитивный сценарий")
    void create() throws Exception {
        HistoryDto expectedResult = getHistoryDto();

        String detailsDtoJson = objectMapper.writeValueAsString(expectedResult);

        when(historyService.create(expectedResult)).thenReturn(expectedResult);

        MvcResult result = mockMvc.perform(post("/api/history")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(detailsDtoJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();

        HistoryDto actualResult = new ObjectMapper().readValue(responseContent, HistoryDto.class);

        assertEquals(expectedResult, actualResult);
        verify(historyService, times(1)).create(expectedResult);
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void update() throws Exception {
            HistoryDto expectedResult = getHistoryDto();

            String detailsDtoJson = objectMapper.writeValueAsString(expectedResult);

            Long id = 111L;

            when(historyService.update(id, expectedResult)).thenReturn(expectedResult);

            MvcResult result = mockMvc.perform(put("/api/history/" + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(detailsDtoJson))
                    .andExpect(status().isOk())
                    .andReturn();

            String responseContent = result.getResponse().getContentAsString();

            HistoryDto actualResult = new ObjectMapper().readValue(responseContent, HistoryDto.class);

            assertEquals(expectedResult, actualResult);
            verify(historyService, times(1)).update(id, expectedResult);
    }

    List<HistoryDto> getListDTO() {
        HistoryDto entity11 = new HistoryDto(111L,111L,111L
                ,111L,111L,111L,111L);

        HistoryDto entity22 = new HistoryDto(222L,111L,111L
                ,111L,111L,111L,111L);

        HistoryDto entity33 = new HistoryDto(333L,111L,111L
                ,111L,111L,111L,111L);

        List<HistoryDto> dtoList = new ArrayList<>();

        dtoList.add(entity11);
        dtoList.add(entity22);
        dtoList.add(entity33);

        return dtoList;

    }
    HistoryDto getHistoryDto() {
        HistoryDto historyDto = new HistoryDto(111L,111L,111L
                ,111L,111L,111L,111L);
        return historyDto;
    }
}