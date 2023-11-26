package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.AtmEntity;
import com.bank.publicinfo.entity.BranchEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static java.time.LocalTime.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class AtmMapperTest {

    AtmMapper mapper = Mappers.getMapper(AtmMapper.class);

    BranchEntity branchEntity = new BranchEntity(1L, "BranchAddress", 123456789L, "City",
            of(9, 0), of(17, 0));
    AtmEntity testEntity = new AtmEntity(1L, "AtmAddress"
            , of(8, 0), of(18, 0), true, branchEntity);
    BranchDto branchDto = new BranchDto(1L, "BranchAddress", 123456789L, "City",
            of(9, 0), of(17, 0));
    AtmDto testDto = new AtmDto(1L, "AtmAddress"
            , of(8, 0), of(18, 0), true, branchDto);

    @Test
    @DisplayName("маппиг в энтити")
    void toEntityTest() {
        var actualEntity = mapper.toEntity(testDto);

        assertThat(actualEntity)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(testEntity);
    }

    @Test
    @DisplayName("маппниг в энтити, на вход подан null")
    void toEntityNullTest() {
        var expectedEntity = mapper.toEntity(null);

        assertNull(expectedEntity, "При AtmDto = null ожидается null");
    }

    @Test
    @DisplayName("маппинг в дто")
    void toDtoTest() {
        var actualDto = mapper.toDto(testEntity);

        assertThat(actualDto)
                .usingRecursiveComparison()
                .isEqualTo(testDto);
    }

    @Test
    @DisplayName("маппниг в дто, на вход подан null")
    void toDtoNullTest() {
        var expectedDto = mapper.toDto(null);

        assertThat(expectedDto)
                .as("Ожидался null")
                .isNull();
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
        List<AtmEntity> entityList = new ArrayList<>();
        entityList.add(testEntity);

        List<AtmDto> dtoList = mapper.toDtoList(entityList);

        assertThat(dtoList)
                .as("Результат маппинга не должен быть null")
                .isNotNull()
                .as("Размеры списков не совпадают")
                .hasSameSizeAs(entityList);
    }

    @Test
    @DisplayName("маппинг в список дто, на вход подан null")
    void entityNullListToDtoListTest() {
        List<AtmDto> dtoList = mapper.toDtoList(null);

        assertThat(dtoList)
                .as("Ожидался null, но фактический результат: " + dtoList)
                .isNull();
    }
}