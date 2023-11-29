package com.bank.antifraud.mappers;

import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.entity.SuspiciousCardTransferEntity;
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
class SuspiciousCardTransferMapperTest {

    SuspiciousCardTransferMapper mapper = Mappers.getMapper(SuspiciousCardTransferMapper.class);

    SuspiciousCardTransferDto dtoResult = new SuspiciousCardTransferDto(10L,
            13L, false, false,
            "blockedReason", "suspiciousReason");

    SuspiciousCardTransferEntity  entityResult = new SuspiciousCardTransferEntity(10L,
            13L, false, false,
            "blockedReason", "suspiciousReason");

    @Test
    @DisplayName("маппинг в Dto")
    void toDto() {
        SuspiciousCardTransferDto actualResult = mapper.toDto(entityResult);

        assertEquals(actualResult,dtoResult);
    }

    @Test
    @DisplayName("маппинг в Dto, на вход подан null")
    void toDtoNull() {
        SuspiciousCardTransferDto actualResultNull = mapper.toDto(null);

        assertNull(actualResultNull);
    }

    @Test
    @DisplayName("маппинг в Entity")
    void toEntity() {
        SuspiciousCardTransferEntity actualResult = mapper.toEntity(dtoResult);

        assertNull(actualResult.getId());

        actualResult.setId(10L);

        assertEquals(actualResult,entityResult);
    }

    @Test
    @DisplayName("маппинг в Entity, на вход подан null")
    void toEntityNull() {
        SuspiciousCardTransferEntity actualResultNull = mapper.toEntity(null);

        assertNull(actualResultNull);
    }

    @Test
    @DisplayName("маппинг в Dto List")
    void toListDto() {
        List<SuspiciousCardTransferEntity> entityList = new ArrayList<>();

        entityList.add(entityResult);

        List<SuspiciousCardTransferDto> actualResult = mapper.toListDto(entityList);

        List<SuspiciousCardTransferDto> expectedResult = new ArrayList<>();

        expectedResult.add(dtoResult);

        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("маппинг в Dto List, на вход подан null")
    void toListDtoNull() {
        List<SuspiciousCardTransferDto> actualResultNull = mapper.toListDto(null);

        assertNull(actualResultNull);
    }

    @Test
    @DisplayName("слияние в Entity")
    void mergeToEntity() {
        SuspiciousCardTransferDto cardTransferDto = new SuspiciousCardTransferDto(20L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        SuspiciousCardTransferEntity actualResult = mapper.mergeToEntity(cardTransferDto, entityResult);

        SuspiciousCardTransferEntity expectedResult = new SuspiciousCardTransferEntity(10L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("слияние в Entity, на вход подан null")
    void mergeToEntityNull() {
        SuspiciousCardTransferEntity actualResult = mapper.mergeToEntity(null, entityResult);

        SuspiciousCardTransferEntity expectedResult = new SuspiciousCardTransferEntity(10L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        assertEquals(actualResult, expectedResult);
    }
}