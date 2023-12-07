package com.bank.authorization.controller;

import com.bank.authorization.dto.UserDto;
import com.bank.authorization.service.UserService;
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
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserControllerTest {

    @MockBean
    UserService userService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;


    @Test
    @DisplayName("создание User, позитивный сценарий")
    void createUserPositiveTest() throws Exception {
        UserDto expectedResult = getUserDto();
        String userDtoJson = objectMapper.writeValueAsString(expectedResult);
        when(userService.save(expectedResult)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isCreated())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();

        UserDto actualResult = new ObjectMapper().readValue(responseContent, UserDto.class);

        assertEquals(expectedResult, actualResult);
        verify(userService, times(1)).save(expectedResult);
    }

    @Test
    @DisplayName("чтение по id, позитивный сценарий")
    void readByIdPositiveTest() throws Exception {
        UserDto expectedResult = getUserDto();
        Long id = expectedResult.getId();
        when(userService.findById(id)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(get("/read/" + id))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();
        UserDto actualResult = new ObjectMapper().readValue(responseContent, UserDto.class);

        assertEquals(expectedResult, actualResult);
        verify(userService, times(1)).findById(id);
    }

    @Test
    @DisplayName("чтение по id с неправильным запросом, негативный сценарий")
    void readByIdBadRequestNegativeTest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/read/fake"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(exception -> exception.getResolvedException()
                        .getClass().equals(MethodArgumentTypeMismatchException.class))
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertEquals(response, "Проверьте корректность применяемого запроса.");
    }

    @Test
    @DisplayName("обновление User по id, позитивный сценарий")
    void updateByIdPositiveTest() throws Exception{
        UserDto expectedResult = getUserDto();
        Long id = expectedResult.getId();
        String userDtoJson = objectMapper.writeValueAsString(expectedResult);
        when(userService.update(id, expectedResult)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(put("/" + id + "/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userDtoJson))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();

        UserDto actualResult = new ObjectMapper().readValue(responseContent, UserDto.class);

        assertEquals(expectedResult, actualResult);
        verify(userService, times(1)).update(id, expectedResult);
    }

    @Test
    @DisplayName("обновление пользователя с неправильным методом запроса, негативный сценарий")
    void updateUserMethodNotAllowedNegativeTest() throws Exception {
        UserDto expectedResult = getUserDto();
        Long id = expectedResult.getId();
        String userDtoJson = objectMapper.writeValueAsString(expectedResult);
        when(userService.update(id, expectedResult)).thenReturn(expectedResult);
        MvcResult mvcResult = mockMvc.perform(get("/" + id + "/update"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(exception -> exception.getResolvedException()
                        .getClass().equals(HttpRequestMethodNotSupportedException.class))
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();
        assertEquals(response, "Выбранный вами метод не поддерживается.");
    }


    @Test
    @DisplayName("чтение по списку ids, позитивный сценарий")
    void readAllByListIdsPositiveTest() throws Exception {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
        List<UserDto> expectedResult = getUserDtoList();
        String idsJson = objectMapper.writeValueAsString(ids);
        when(userService.findAllByIds(ids)).thenReturn(expectedResult);
        MvcResult result = mockMvc.perform(get("/read/all?ids=1,2,3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(idsJson))
                .andExpect(status().isOk())
                .andReturn();
        String responseContent = result.getResponse().getContentAsString();

        List<UserDto> actualResult = new ObjectMapper().readValue(responseContent, new TypeReference<>() {});

        assertEquals(expectedResult, actualResult);
        verify(userService, times(1)).findAllByIds(ids);
    }

    private List<UserDto> getUserDtoList() {
        return new ArrayList<>(Arrays.asList(new UserDto(1L, "USER", "password", 99L),
                new UserDto(2L, "USER", "password", 99L),
                new UserDto(3L, "USER", "password", 99L)));
    }

    private UserDto getUserDto() {
        return new UserDto(20L, "USER", "password", 99L);
    }
}