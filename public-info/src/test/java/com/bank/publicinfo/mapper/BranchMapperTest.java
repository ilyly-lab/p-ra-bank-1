package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BranchDto;
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

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class BranchMapperTest {

    BranchMapper mapper = Mappers.getMapper(BranchMapper.class);
    BranchEntity testEntity = new BranchEntity(1L, "BranchAddress", 123456789L, "City",
            of(9, 0), of(17, 0));
    BranchDto testDto = new BranchDto(1L, "BranchAddress", 123456789L, "City",
            of(9, 0), of(17, 0));

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
        var expectedEntity = mapper.toEntity(null);

        assertThat(expectedEntity)
                .as("Ожидался null")
                .isNull();
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
    @DisplayName("маппинг в дто, на вход подан null")
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
        var expectedEntity = mapper.mergeToEntity(null, testEntity);

        assertThat(expectedEntity).isEqualTo(testEntity);
    }

    @Test
    @DisplayName("маппинг в список дто")
    void toDtoListTest() {
        List<BranchEntity> entityList = new ArrayList<>();
        entityList.add(testEntity);

        List<BranchDto> dtoList = mapper.toDtoList(entityList);

        assertThat(dtoList)
                .as("Результат маппинга не должен быть null")
                .isNotNull()
                .as("Размеры списков не совпадают")
                .hasSameSizeAs(entityList);
    }

    @Test
    @DisplayName("маппинг в список дто, на вход подан null")
    void toDtoListNullTest() {
        List<BranchDto> dtoList = mapper.toDtoList(null);

        assertThat(dtoList)
                .as("Ожидался null, но фактический результат: " + dtoList)
                .isNull();
    }
}