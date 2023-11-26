package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.AuditEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AuditMapperTest {

    AuditMapper mapper = Mappers.getMapper(AuditMapper.class);

    AuditEntity testEntity = new AuditEntity(1L, "Entity", "create",
            "user", "admin", new Timestamp(100L), new Timestamp(200L),
            "NewEntityJson", "EntityJson");
    AuditDto testDto = new AuditDto(1L, "Entity", "create",
            "user", "admin", new Timestamp(100L), new Timestamp(200L),
            "NewEntityJson", "EntityJson");

    @Test
    @DisplayName("маппинг в дто")
    void toDtoTest() {
        var actualDto = mapper.toDto(testEntity);

        assertThat(actualDto)
                .usingRecursiveComparison()
                .isEqualTo(testDto);
    }

    @Test
    @DisplayName("маппинг в дто, на вход подан null")
    void toDtoNullTest() {
        var expectedDto = mapper.toDto(null);

        assertNull(expectedDto, "При AuditEntity = null ожидается null");
    }
}