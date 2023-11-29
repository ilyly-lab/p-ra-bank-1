package com.bank.antifraud.mappers;

import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.entity.SuspiciousAccountTransferEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class SuspiciousAccountTransferMapperTest {

    SuspiciousAccountTransferMapper mapper = Mappers.getMapper(SuspiciousAccountTransferMapper.class);

    SuspiciousAccountTransferDto dtoResult = new SuspiciousAccountTransferDto(10L, 13L,
            false, false, "blockedReason", "suspiciousReason");

    SuspiciousAccountTransferEntity entityResult = new SuspiciousAccountTransferEntity(10L, 13L,
            false, false, "blockedReason", "suspiciousReason");
    @Test
    @DisplayName("маппинг в Dto")
    void toDto() {
        SuspiciousAccountTransferDto actualResult = mapper.toDto(entityResult);

        assertEquals(actualResult,dtoResult);
    }

    @Test
    @DisplayName("маппинг в Dto, на вход подан null")
    void toDtoNull() {
        SuspiciousAccountTransferDto actualResultNull = mapper.toDto(null);

        assertNull(actualResultNull);
    }

    @Test
    @DisplayName("маппинг в Entity")
    void toEntity() {
        SuspiciousAccountTransferEntity actualResult = mapper.toEntity(dtoResult);

        assertNull(actualResult.getId());

        actualResult.setId(10L);

        assertEquals(actualResult,entityResult);
    }

    @Test
    @DisplayName("маппинг в Entity, на вход подан null")
    void toEntityNull() {
        SuspiciousAccountTransferEntity actualResultNull = mapper.toEntity(null);

        assertNull(actualResultNull);
    }

    @Test
    @DisplayName("маппинг в Dto List")
    void toListDto() {
        List<SuspiciousAccountTransferEntity> entityList = new ArrayList<>();

        entityList.add(entityResult);

        List<SuspiciousAccountTransferDto> actualResult = mapper.toListDto(entityList);

        List<SuspiciousAccountTransferDto> expectedResult = new ArrayList<>();

        expectedResult.add(dtoResult);

        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("маппинг в Dto List, на вход подан null")
    void toListDtoNull() {
        List<SuspiciousAccountTransferDto> actualResultNull = mapper.toListDto(null);

        assertNull(actualResultNull);
    }

    @Test
    @DisplayName("слияние в Entity")
    void mergeToEntity() {
        SuspiciousAccountTransferDto accountTransferDto = new SuspiciousAccountTransferDto(20L,
                13L, false, false, "blockedReason",
                "suspiciousReason");

        SuspiciousAccountTransferEntity actualResult = mapper.mergeToEntity(accountTransferDto, entityResult);

        SuspiciousAccountTransferEntity expectedResult = new SuspiciousAccountTransferEntity(10L,
                13L, false, false, "blockedReason",
                "suspiciousReason");

        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("слияние в Entity, на вход подан null")
    void mergeToEntityNull() {
        SuspiciousAccountTransferEntity actualResult = mapper.mergeToEntity(null, entityResult);

        SuspiciousAccountTransferEntity expectedResult = new SuspiciousAccountTransferEntity(10L,
                13L, false, false, "blockedReason",
                "suspiciousReason");

        assertEquals(actualResult, expectedResult);
    }
}