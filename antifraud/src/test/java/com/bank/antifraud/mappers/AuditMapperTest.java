package com.bank.antifraud.mappers;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entity.AuditEntity;
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
class AuditMapperTest {

    AuditMapper mapper = Mappers.getMapper(AuditMapper.class);

    @Test
    @DisplayName("маппинг в Dto")
    public void toDto() {
        AuditEntity auditEntity = new AuditEntity(10L, "entityType",
                "operationType", "createdBy", "modifiedBy",
                new Timestamp(10L), new Timestamp(20L),
                "newEntityJson", "entityJson");

        AuditDto actualResult = mapper.toDto(auditEntity);

        assertNotNull(actualResult);

        AuditDto expectedResult = new AuditDto(10L, "entityType",
                "operationType", "createdBy", "modifiedBy",
                new Timestamp(10L), new Timestamp(20L),
                "newEntityJson", "entityJson");

        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("маппинг в Dto, на вход подан null")
    public void toDtoNull() {
        AuditEntity auditEntity = null;

        AuditDto actualResult = mapper.toDto(auditEntity);

        assertNull(actualResult);
    }
}