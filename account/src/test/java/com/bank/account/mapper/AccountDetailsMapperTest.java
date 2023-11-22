package com.bank.account.mapper;

import com.bank.account.dto.AccountDetailsDto;
import com.bank.account.entity.AccountDetailsEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AccountDetailsMapperTest {

    AccountDetailsMapper mapper = Mappers.getMapper(AccountDetailsMapper.class);

    AccountDetailsDto dtoResult1 = new AccountDetailsDto(10L, 230L, 123L, 790L,
            new BigDecimal("109"), false, 290L);

    AccountDetailsEntity entityResult1 = new AccountDetailsEntity(10L, 230L, 123L,
            790L, new BigDecimal("109"), false, 290L);

    @Test
    @DisplayName("маппинг в энтити")
    void toEntityTest() {
        AccountDetailsEntity actualResult = mapper.toEntity(dtoResult1);

        assertNull(actualResult.getId());

        actualResult.setId(10L);

        assertEquals(actualResult, entityResult1);
    }

    @Test
    @DisplayName("маппинг в энтити, на вход подан null")
    void toEntityNullTest() {
        AccountDetailsEntity actualResultNull = mapper.toEntity(null);

        assertNull(actualResultNull);
    }


    @Test
    @DisplayName("маппинг в дто")
    void toDtoTest() {
        AccountDetailsDto actualResult = mapper.toDto(entityResult1);

        assertEquals(actualResult, dtoResult1);
    }

    @Test
    @DisplayName("маппинг в дто, на вход подан null")
    void toDtoNullTest() {
        AccountDetailsDto actualResultNull = mapper.toDto(null);

        assertNull(actualResultNull);
    }

    @Test
    @DisplayName("маппинг в дто лист")
    void toDtoListTest() {
        List<AccountDetailsEntity> entityList = new ArrayList<>();

        entityList.add(entityResult1);

        List<AccountDetailsDto> actualResult = mapper.toDtoList(entityList);

        List<AccountDetailsDto> expectedResult = new ArrayList<>();

        expectedResult.add(dtoResult1);

        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("маппинг в дто лист, на вход подан null")
    void toDtoListNullTest() {
        List<AccountDetailsDto> actualResultNull = mapper.toDtoList(null);

        assertNull(actualResultNull);
    }

    @Test
    @DisplayName("слияние в энтити")
    void mergeToEntityTest() {

        AccountDetailsDto accountDetailsDto = new AccountDetailsDto(120L, 123L, 645L, 9341L,
                new BigDecimal("432"), true, 1230L);

        AccountDetailsEntity actualResult = mapper.mergeToEntity(entityResult1, accountDetailsDto);

        AccountDetailsEntity expectedResult = new AccountDetailsEntity(10L, 123L, 645L, 9341L,
                new BigDecimal("432"), true, 1230L);

        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("слияние в энтити, на вход подан null")
    void mergeToEntityNullTest() {

        AccountDetailsEntity actualResultNull = mapper.mergeToEntity(entityResult1, null);

        assertEquals(entityResult1, actualResultNull);
    }
}