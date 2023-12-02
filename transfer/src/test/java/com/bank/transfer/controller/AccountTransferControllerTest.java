package com.bank.transfer.controller;

import com.bank.common.handler.GlobalRestExceptionHandler;
import com.bank.transfer.dto.AccountTransferDto;
import com.bank.transfer.service.AccountTransferService;
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

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountTransferController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class AccountTransferControllerTest {
    @Autowired
    GlobalRestExceptionHandler restExceptionHandler;
    @MockBean
    AccountTransferService accountTransferService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("Получение списка переводов по номеру счёта по списку id, позитивный сценарий")
    void readAllByListIdPositiveTest() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        List<AccountTransferDto> expectedDtoList = List.of(getDto1(), getDto2());

        when(accountTransferService.findAllById(ids)).thenReturn(expectedDtoList);

        MvcResult result = mockMvc.perform(get("/account/read/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ids", "1", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        List<AccountTransferDto> actualList = objectMapper.readValue(responseContent,
                new TypeReference<>() {
                });

        assertEquals(expectedDtoList, actualList);
        verify(accountTransferService, times(1)).findAllById(ids);
    }

    @Test
    @DisplayName("Получение списка переводов по номеру счёта по списку id, негативный сценарий")
    void readAllByListIdNegativeTest() throws Exception {
        List<Long> ids = List.of(1L, 2L);
        EntityNotFoundException exception = new EntityNotFoundException("не найден");

        when(accountTransferService.findAllById(ids)).thenThrow(EntityNotFoundException.class);

        MvcResult result = mockMvc.perform(get("/account/read/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("ids", "1", "2"))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals(restExceptionHandler.handleEntityNotFound(exception).getStatusCodeValue(),
                result.getResponse().getStatus());
        verify(accountTransferService, times(1)).findAllById(ids);
    }

    @Test
    @DisplayName("Получение перевода по номеру счёта по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        Long id = 1L;
        AccountTransferDto expectedDto = new AccountTransferDto();

        when(accountTransferService.findById(id)).thenReturn(expectedDto);

        MvcResult result = mockMvc.perform(get("/account/read/{id}", id))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        AccountTransferDto actualDto = objectMapper.readValue(response, AccountTransferDto.class);

        assertEquals(actualDto, expectedDto);
        verify(accountTransferService, times(1)).findById(id);
    }

    @Test
    @DisplayName("Получение перевода по номеру счёта по id, негативный сценарий")
    void readByIdNegativeTest() throws Exception {
        Long id = 1L;

        when(accountTransferService.findById(id)).thenThrow(EntityNotFoundException.class);

        MvcResult result = mockMvc.perform(get("/account/read/{id}", id))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals(404, result.getResponse().getStatus());
        verify(accountTransferService, times(1)).findById(id);
    }

    @Test
    @DisplayName("Создание перевода по номеру счёта, позитивный сценарий")
    void createAccountTransferDtoPositiveTest() throws Exception {
        AccountTransferDto expectedDto = getDto1();
        String accountTransferDtoJson = objectMapper.writeValueAsString(expectedDto);

        when(accountTransferService.save(expectedDto)).thenReturn(expectedDto);

        MvcResult result = mockMvc.perform(post("/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountTransferDtoJson))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        AccountTransferDto actualResult = objectMapper.readValue(response, AccountTransferDto.class);

        assertEquals(actualResult, expectedDto);
        verify(accountTransferService, times(1)).save(expectedDto);
    }

    @Test
    @DisplayName("Создание перевода по номеру счёта, негативный сценарий")
    void createAccountTransferDtoNegativeTest() throws Exception {
        AccountTransferDto expectedDto = getDto1();
        String accountTransferDtoJson = objectMapper.writeValueAsString(expectedDto);
        UnsupportedOperationException exception =
                new UnsupportedOperationException("Вы ввели недопустимые значения.");

        when(accountTransferService.save(expectedDto)).thenThrow(exception);

        MvcResult result = mockMvc.perform(post("/account/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountTransferDtoJson))
                .andExpect(status().isNotAcceptable())
                .andReturn();

        assertEquals(exception.getMessage(), result.getResponse().getContentAsString());
        verify(accountTransferService, times(1)).save(expectedDto);
    }

    @Test
    @DisplayName("Обновление перевода по номеру счёта по id, позитивный сценарий")
    void updateAccountTransferDtoPositiveTest() throws Exception {
        Long id = 1L;
        AccountTransferDto expectedDto = new AccountTransferDto();
        String accountTransferDtoJson = objectMapper.writeValueAsString(expectedDto);

        when(accountTransferService.update(id, expectedDto)).thenReturn(expectedDto);

        MvcResult result = mockMvc.perform(put("/account/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountTransferDtoJson))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        AccountTransferDto actualResult = objectMapper.readValue(response, AccountTransferDto.class);

        assertEquals(actualResult, expectedDto);
        verify(accountTransferService, times(1)).update(id, expectedDto);
    }

    @Test
    @DisplayName("Обновление перевода по номеру счёта по id, негативный сценарий")
    void updateAccountTransferDtoNegativeTest() throws Exception {
        Long id = 1L;
        AccountTransferDto expectedDto = new AccountTransferDto();
        String accountTransferDtoJson = objectMapper.writeValueAsString(expectedDto);
        EntityNotFoundException exception = new EntityNotFoundException("не найден");

        when(accountTransferService.update(id, expectedDto)).thenThrow(exception);

        MvcResult result = mockMvc.perform(put("/account/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(accountTransferDtoJson))
                .andExpect(status().isNotFound())
                .andReturn();

        assertEquals(result.getResponse().getStatus(),
                restExceptionHandler.handleEntityNotFound(exception).getStatusCodeValue());
        verify(accountTransferService, times(1)).update(id, expectedDto);
    }

    private AccountTransferDto getDto1() {
        AccountTransferDto transferDto = new AccountTransferDto();
        transferDto.setAccountNumber(123456L);
        transferDto.setAmount(BigDecimal.valueOf(100));
        transferDto.setPurpose("Test purpose");
        transferDto.setAccountDetailsId(1L);
        return transferDto;
    }


    private AccountTransferDto getDto2() {
        return AccountTransferDto.builder()
                .accountNumber(2L)
                .amount(BigDecimal.valueOf(7777L))
                .purpose("Test purpose")
                .accountDetailsId(2L)
                .build();
    }
}