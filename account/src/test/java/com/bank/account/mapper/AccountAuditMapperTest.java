package com.bank.account.mapper;

import com.bank.account.dto.AuditDto;
import com.bank.account.entity.AuditEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AccountAuditMapperTest {

    AccountAuditMapper mapper = Mappers.getMapper(AccountAuditMapper.class);

    @Test
    @DisplayName("маппинг в дто")
    public void toDtoTest() {
        AuditEntity entity = new AuditEntity(10L, "entityType", "operationType",
                "createdBy", "modifiedBy", new Timestamp(10L), new Timestamp(20L),
                "newEntityJson", "entityJson");

        AuditDto actualResult = mapper.toDto(entity);

        assertNotNull(actualResult);

        AuditDto expectedResult = new AuditDto(10L, "entityType", "operationType",
                "createdBy", "modifiedBy", new Timestamp(10L), new Timestamp(20L),
                "newEntityJson", "entityJson");

        assertEquals(actualResult, expectedResult);
    }

    @Test
    @DisplayName("маппинг в дто, на вход подан null")
    public void toDtoNullTest() {
        AuditEntity entity = null;

        AuditDto actualResult = mapper.toDto(entity);

        assertNull(actualResult);
    }
}