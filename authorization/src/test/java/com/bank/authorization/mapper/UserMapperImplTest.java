package com.bank.authorization.mapper;

import com.bank.authorization.dto.UserDto;
import com.bank.authorization.entity.UserEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class UserMapperImplTest {

    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    @DisplayName("маппинг сущности в дто, позитивный сценарий")
    void toDTOTest() {
        UserEntity entity = getUserEntity();
        UserDto expectedResult = getUserDto();

        UserDto actualResult = mapper.toDTO(entity);

        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("маппинг сущности в дто с нулевым аргументом, негативный сценарий")
    void toDTONullTest() {
        UserDto actualResult = mapper.toDTO(null);

        assertNull(actualResult);
    }

    @Test
    @DisplayName("маппинг дто в сущность, позитивный сценарий")
    void toEntityTest() {
        UserEntity expectedResult = getUserEntity();
        UserDto entity = getUserDto();

        UserEntity actualResult = mapper.toEntity(entity);

        assertEquals(actualResult.getProfileId(), expectedResult.getProfileId());
        assertEquals(actualResult.getRole(), expectedResult.getRole());
        assertEquals(actualResult.getPassword(), expectedResult.getPassword());
    }

    @Test
    @DisplayName("маппинг дто в сущность с нулевым аргументом, негативный сценарий")
    void toEntityNullTest() {
        UserEntity actualResult = mapper.toEntity(null);

        assertNull(actualResult);
    }

    @Test
    @DisplayName("маппинг списка сущностей в список дто, позитивный сценарий")
    void toDtoListTest() {
        List<UserEntity> listEntity = getUserEntityList();
        List<UserDto> expectedResult = getUserDtoList();

        List<UserDto> actualResult = mapper.toDtoList(listEntity);

        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("маппинг списка сущностей в список дто с нулевым аргументом, негативный сценарий")
    void toDtoListNullTest() {
        List<UserDto> actualResult = mapper.toDtoList(null);

        assertNull(actualResult);
    }

    @Test
    @DisplayName("слияние дто и сущности, позитивный сценарий")
    void mergeToEntityTest() {
        UserEntity userEntity = getUserEntity();
        userEntity.setRole("ADMIN");
        userEntity.setProfileId(66L);
        userEntity.setPassword("fake");
        UserDto userDto = getUserDto();
        UserEntity expectedResult = getUserEntity();

        UserEntity actualResult = mapper.mergeToEntity(userDto, userEntity);

        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("слияние дто и сущности с нулевым аргументом, негативный сценарий")
    void mergeToEntityNullTest() {
        UserEntity expectedResult = getUserEntity();

        UserEntity actualResult = mapper.mergeToEntity(null, expectedResult);

        assertEquals(actualResult, expectedResult);
    }

    List<UserDto> getUserDtoList() {
        return new ArrayList<>(Arrays.asList(new UserDto(1L, "USER", "password", 99L),
                new UserDto(2L, "USER", "password", 99L),
                new UserDto(3L, "USER", "password", 99L)));
    }

    List<UserEntity> getUserEntityList() {
        return new ArrayList<>(Arrays.asList(new UserEntity(1L, "USER", 99L, "password"),
                new UserEntity(2L, "USER", 99L, "password"),
                new UserEntity(3L, "USER", 99L, "password")));
    }

    UserDto getUserDto() {
        return new UserDto(20L, "USER", "password", 99L);
    }

    UserEntity getUserEntity() {
        return new UserEntity(20L, "USER", 99L, "password");
    }
}