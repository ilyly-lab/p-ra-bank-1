package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.entity.BankDetailsEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class BankDetailsMapperTest {

    BankDetailsMapper mapper = Mappers.getMapper(BankDetailsMapper.class);

    BankDetailsEntity testEntity = new BankDetailsEntity(1L, 101L, 102L, 103L,
            new BigDecimal("123"), "City", "Stock", "Name");
    BankDetailsDto testDto = new BankDetailsDto(1L, 101L, 102L, 103L,
            new BigDecimal("123"), "City", "Stock", "Name");


    @Test
    @DisplayName("маппинг в энтити")
    void toEntityTest() {
        var actualEntity = mapper.toEntity(testDto);

        assertThat(actualEntity)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(testEntity);
    }

    @Test
    @DisplayName("маппинг в энтити, на вход подан null")
    void toEntityNullTest() {
        var actualEntity = mapper.toEntity(null);

        assertNull(actualEntity);
    }

    @Test
    @DisplayName("маппинг в дто")
    void toDtoTest() {
        var actualDto = mapper.toDto(testEntity);

        assertThat(actualDto)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(testDto);
    }

    @Test
    @DisplayName("маппинг в дто, на вход подан null")
    void toDtoNullTest() {
        var expectedDto = mapper.toDto(null);

        assertNull(expectedDto);
    }

    @Test
    @DisplayName("слияние в энтити")
    void mergeToEntityTest() {
        var mergedEntity = mapper.mergeToEntity(testDto, testEntity);

        assertThat(mergedEntity)
                .usingRecursiveComparison()
                .isEqualTo(testEntity);
    }

    @Test
    @DisplayName("слияние в энтити, на вход подан null")
    void mergeToEntityNullTest() {
        var mergedEntity = mapper.mergeToEntity(null, testEntity);

        assertThat(mergedEntity).isEqualTo(testEntity);
    }

    @Test
    @DisplayName("маппинг в список дто")
    void toDtoListTest() {
        List<BankDetailsEntity> entityList = new ArrayList<>();
        entityList.add(testEntity);

        List<BankDetailsDto> dtoList = mapper.toDtoList(entityList);

        assertThat(dtoList)
                .as("Результат маппинга не должен быть null")
                .isNotNull()
                .as("Размеры списков не совпадают")
                .hasSameSizeAs(entityList);
    }

    @Test
    @DisplayName("маппинг в список дто, на вход подан null")
    void toDtoListNullTest() {
        List<BankDetailsDto> dtoList = mapper.toDtoList(null);

        assertThat(dtoList)
                .as("Ожидался null, но фактический результат: " + dtoList)
                .isNull();
    }
}