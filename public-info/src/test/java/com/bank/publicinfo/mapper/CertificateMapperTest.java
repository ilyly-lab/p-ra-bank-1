package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.CertificateDto;
import com.bank.publicinfo.entity.BankDetailsEntity;
import com.bank.publicinfo.entity.CertificateEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class CertificateMapperTest {

    CertificateMapper mapper = Mappers.getMapper(CertificateMapper.class);

    BankDetailsEntity bankDetailsEntity = new BankDetailsEntity(1L, 101L, 102L, 103L,
            new BigDecimal("123"), "City", "Stock", "Name");
    BankDetailsDto bankDetailsDto = new BankDetailsDto(1L, 101L, 102L, 103L,
            new BigDecimal("123"), "City", "Stock", "Name");
    CertificateEntity testEntity = new CertificateEntity(1L, new Byte[]{10, 20, 30, 40}, bankDetailsEntity);
    CertificateDto testDto = new CertificateDto(1L, new Byte[]{10, 20, 30, 40}, bankDetailsDto);

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

        assertThat(actualEntity)
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
    void toDtoNullInputTest() {
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
        List<CertificateEntity> entityList = new ArrayList<>();
        entityList.add(testEntity);

        List<CertificateDto> dtoList = mapper.toDtoList(entityList);

        assertThat(dtoList)
                .as("Результат маппинга не должен быть null")
                .isNotNull()
                .as("Размеры списков не совпадают")
                .hasSameSizeAs(entityList);
    }

    @Test
    @DisplayName("маппинг в список дто, на вход подан null")
    void toDtoListNullTest() {
        List<CertificateDto> dtoList = mapper.toDtoList(null);

        assertThat(dtoList)
                .as("Ожидался null, но фактический результат: " + dtoList)
                .isNull();
    }
}