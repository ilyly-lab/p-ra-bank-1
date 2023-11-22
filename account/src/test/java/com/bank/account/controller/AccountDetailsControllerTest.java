package com.bank.account.controller;

import com.bank.account.dto.AccountDetailsDto;
import com.bank.account.service.AccountDetailsServiceImpl;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountDetailsController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class AccountDetailsControllerTest {

    @MockBean
    AccountDetailsServiceImpl accountDetailsService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        Long accountId = 1L;
        AccountDetailsDto expectedResult = getDetailsDto();

        when(accountDetailsService.findById(accountId)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(get("/details/" + accountId))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        AccountDetailsDto actualResult = new ObjectMapper().readValue(responseContent, AccountDetailsDto.class);

        assertEquals(expectedResult, actualResult);
        verify(accountDetailsService, times(1)).findById(accountId);
    }

    @Test
    @DisplayName("создание AccountDetails, позитивный сценарий")
    void createByAccountDetailsPositiveTest() throws Exception {
        AccountDetailsDto expectedResult = getDetailsDto();
        String detailsDtoJson = objectMapper.writeValueAsString(expectedResult);

        when(accountDetailsService.save(expectedResult)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(post("/details/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(detailsDtoJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        AccountDetailsDto actualResult = new ObjectMapper().readValue(responseContent, AccountDetailsDto.class);

        assertEquals(expectedResult, actualResult);
        verify(accountDetailsService, times(1)).save(expectedResult);
    }

    @Test
    @DisplayName("обновление AccountDetails по id, позитивный сценарий")
    void updateByAccountDetailsAndIdPositiveTest() throws Exception {
        AccountDetailsDto expectedResult = getDetailsDto();
        String detailsDtoJson = objectMapper.writeValueAsString(expectedResult);

        Long id = 1L;

        when(accountDetailsService.update(id, expectedResult)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(put("/details/update/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(detailsDtoJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        AccountDetailsDto actualResult = new ObjectMapper().readValue(responseContent, AccountDetailsDto.class);

        assertEquals(expectedResult, actualResult);
        verify(accountDetailsService, times(1)).update(id, expectedResult);
    }

    @Test
    @DisplayName("чтение по списку ids, позитивный сценарий")
    void readAllByIdsPositiveTest() throws Exception {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        List<AccountDetailsDto> expectedResult = getDetailsDtoList();
        String idsJson = objectMapper.writeValueAsString(ids);


        when(accountDetailsService.findAllById(ids)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(get("/details/read/all?ids=1,2,3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(idsJson))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        List<AccountDetailsDto> actualResult = new ObjectMapper().readValue(responseContent,
                new TypeReference<>() {
                }
        );

        assertEquals(expectedResult, actualResult);
        verify(accountDetailsService, times(1)).findAllById(ids);
    }

    private AccountDetailsDto getDetailsDto() {
        return new AccountDetailsDto(1L, 230L, 123L, 790L,
                new BigDecimal("109"), false, 290L);
    }

    private List<AccountDetailsDto> getDetailsDtoList() {
        return new ArrayList<>(Arrays.asList(
                new AccountDetailsDto(1L, 230L, 123L, 790L,
                        new BigDecimal("109"), false, 290L),
                new AccountDetailsDto(2L, 230L, 123L, 790L,
                        new BigDecimal("109"), false, 290L),
                new AccountDetailsDto(3L, 230L, 123L, 790L,
                        new BigDecimal("109"), false, 290L)
        ));
    }

}