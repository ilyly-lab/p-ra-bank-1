package com.bank.transfer.mapper;

import com.bank.transfer.dto.AuditDto;
import com.bank.transfer.entity.AuditEntity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AuditMapperImplTest {
    private final AuditMapper auditMapper = new AuditMapperImpl();

    @Test
    @DisplayName("Маппинг в Dto и проверка на null")
    void toDtoPositiveAndNullTest() {
        AuditEntity entity = AuditEntity.builder()
                .id(1L)
                .entityType("entityType")
                .entityJson("entityJson")
                .operationType("operationType")
                .createdBy("createdBy")
                .modifiedBy("modifiedBy")
                .createdAt(new Timestamp(111))
                .modifiedAt(new Timestamp(1232))
                .newEntityJson("newEntityJson")
                .entityJson("entityJson")
                .build();

        AuditDto dto = AuditDto.builder()
                .id(1L)
                .entityType("entityType")
                .entityJson("entityJson")
                .operationType("operationType")
                .createdBy("createdBy")
                .modifiedBy("modifiedBy")
                .createdAt(new Timestamp(111))
                .modifiedAt(new Timestamp(1232))
                .newEntityJson("newEntityJson")
                .entityJson("entityJson")
                .build();

        AuditDto actualDto = auditMapper.toDto(entity);
        AuditDto nullActualDto = auditMapper.toDto(null);

        assertAll(
                () -> assertNull(nullActualDto),
                () -> assertEquals(actualDto.getEntityType(), dto.getEntityType()),
                () -> assertEquals(actualDto.getEntityJson(), dto.getEntityJson()),
                () -> assertEquals(actualDto.getOperationType(), dto.getOperationType()),
                () -> assertEquals(actualDto.getCreatedBy(), dto.getCreatedBy()),
                () -> assertEquals(actualDto.getModifiedBy(), dto.getModifiedBy()),
                () -> assertEquals(actualDto.getCreatedAt(), dto.getCreatedAt()),
                () -> assertEquals(actualDto.getModifiedAt(), dto.getModifiedAt()),
                () -> assertEquals(actualDto.getNewEntityJson(), dto.getNewEntityJson()),
                () -> assertEquals(actualDto.getEntityJson(), dto.getEntityJson())
        );
    }

    @Test
    @DisplayName("Маппинг в Entity и проверка на null")
    void toEntityPositiveAndNullTest() {
        AuditEntity entity = AuditEntity.builder()
                .id(1L)
                .entityType("entityType")
                .entityJson("entityJson")
                .operationType("operationType")
                .createdBy("createdBy")
                .modifiedBy("modifiedBy")
                .createdAt(new Timestamp(111))
                .modifiedAt(new Timestamp(1232))
                .newEntityJson("newEntityJson")
                .entityJson("entityJson")
                .build();

        AuditDto dto = AuditDto.builder()
                .id(1L)
                .entityType("entityType")
                .entityJson("entityJson")
                .operationType("operationType")
                .createdBy("createdBy")
                .modifiedBy("modifiedBy")
                .createdAt(new Timestamp(111))
                .modifiedAt(new Timestamp(1232))
                .newEntityJson("newEntityJson")
                .entityJson("entityJson")
                .build();

        AuditEntity actualEntity = auditMapper.toEntity(dto);
        AuditEntity nullActualEntity = auditMapper.toEntity(null);

        assertAll(
                () -> assertNull(nullActualEntity),
                () -> assertEquals(actualEntity.getEntityType(), entity.getEntityType()),
                () -> assertEquals(actualEntity.getEntityJson(), entity.getEntityJson()),
                () -> assertEquals(actualEntity.getOperationType(), entity.getOperationType()),
                () -> assertEquals(actualEntity.getCreatedBy(), entity.getCreatedBy()),
                () -> assertEquals(actualEntity.getModifiedBy(), entity.getModifiedBy()),
                () -> assertEquals(actualEntity.getCreatedAt(), entity.getCreatedAt()),
                () -> assertEquals(actualEntity.getModifiedAt(), entity.getModifiedAt()),
                () -> assertEquals(actualEntity.getNewEntityJson(), entity.getNewEntityJson()),
                () -> assertEquals(actualEntity.getEntityJson(), entity.getEntityJson())
        );
    }
}