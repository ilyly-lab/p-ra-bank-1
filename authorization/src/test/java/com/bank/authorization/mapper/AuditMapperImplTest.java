package com.bank.authorization.mapper;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.entity.AuditEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AuditMapperImplTest {

    AuditMapper mapper = Mappers.getMapper(AuditMapper.class);

    @Test
    @DisplayName("маппинг в дто, позитивный сценарий")
    void toDtoTest() {
        AuditEntity entity = getAuditEntity();
        AuditDto expectedResult = getAuditDto();

        AuditDto actualResult = mapper.toDto(entity);

        assertNotNull(actualResult);
        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("маппинг в дто с аргументом null, негативный сценарий")
    void toDtoNullTest() {
        AuditEntity entity = null;

        AuditDto actualResult = mapper.toDto(entity);

        assertNull(actualResult);
    }

    private AuditDto getAuditDto() {
        return new AuditDto(10l,
                "entityType",
                "operationType",
                "createdBy",
                "modifiedBy",
                new Timestamp(10L),
                new Timestamp(20L),
                "newEntityJson",
                "entityJson");
    }

    private AuditEntity getAuditEntity() {
        return new AuditEntity(10l,
                "entityType",
                "operationType",
                "createdBy",
                "modifiedBy",
                new Timestamp(10L),
                new Timestamp(20L),
                "newEntityJson",
                "entityJson");
    }
}