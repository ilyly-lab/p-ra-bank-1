package com.bank.antifraud.mappers;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entity.SuspiciousPhoneTransferEntity;
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
class SuspiciousPhoneTransferMapperTest {

    SuspiciousPhoneTransferMapper mapper = Mappers.getMapper(SuspiciousPhoneTransferMapper.class);

    SuspiciousPhoneTransferDto dtoResult = new SuspiciousPhoneTransferDto(10L,
            13L, false, false,
            "blockedReason", "suspiciousReason");

    SuspiciousPhoneTransferEntity entityResult = new SuspiciousPhoneTransferEntity(10L,
            13L, false, false,
            "blockedReason", "suspiciousReason");

    @Test
    @DisplayName("маппинг в Dto")
    void toDto() {
        SuspiciousPhoneTransferDto actualResult = mapper.toDto(entityResult);

        assertEquals(actualResult,dtoResult);
    }

    @Test
    @DisplayName("маппинг в Dto, на вход подан null")
    void toDtoNull() {
        SuspiciousPhoneTransferDto actualResultNull = mapper.toDto(null);

        assertNull(actualResultNull);
    }

    @Test
    @DisplayName("маппинг в Entity")
    void toEntity() {
        SuspiciousPhoneTransferEntity actualResult = mapper.toEntity(dtoResult);

        assertNull(actualResult.getId());

        actualResult.setId(10L);

        assertEquals(actualResult,entityResult);
    }

    @Test
    @DisplayName("маппинг в Entity, на вход подан null")
    void toEntityNull() {
        SuspiciousPhoneTransferEntity actualResultNull = mapper.toEntity(null);

        assertNull(actualResultNull);
    }

    @Test
    @DisplayName("маппинг в Dto List")
    void toListDto() {
        List<SuspiciousPhoneTransferEntity> entityList = new ArrayList<>();

        entityList.add(entityResult);

        List<SuspiciousPhoneTransferDto> actualResult = mapper.toListDto(entityList);

        List<SuspiciousPhoneTransferDto> expectedResult = new ArrayList<>();

        expectedResult.add(dtoResult);

        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("маппинг в Dto , на вход подан null")
    void toListDtoNull() {
        List<SuspiciousPhoneTransferDto> actualResultNull = mapper.toListDto(null);

        assertNull(actualResultNull);
    }

    @Test
    @DisplayName("слияние в Entity")
    void mergeToEntity() {
        SuspiciousPhoneTransferDto phoneTransferDto = new SuspiciousPhoneTransferDto(20L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        SuspiciousPhoneTransferEntity actualResult = mapper.mergeToEntity(phoneTransferDto, entityResult);

        SuspiciousPhoneTransferEntity expectedResult = new SuspiciousPhoneTransferEntity(10L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("слияние в Entity, на вход подан null")
    void mergeToEntityNull() {
        SuspiciousPhoneTransferEntity actualResult = mapper.mergeToEntity(null, entityResult);

        SuspiciousPhoneTransferEntity expectedResult = new SuspiciousPhoneTransferEntity(10L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        assertEquals(actualResult, expectedResult);
    }
}