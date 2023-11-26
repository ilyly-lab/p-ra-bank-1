package com.bank.publicinfo.mapper;

import com.bank.publicinfo.dto.BankDetailsDto;
import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.entity.BankDetailsEntity;
import com.bank.publicinfo.entity.LicenseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LicenseMapperTest {

    private final LicenseMapper licenseMapper = Mappers.getMapper(LicenseMapper.class);
    private LicenseEntity testEntity;
    private LicenseDto testDto;

    private LicenseEntity createTestEntity() {
        var testEntity = new LicenseEntity();

        testEntity.setId(1L);
        testEntity.setPhotoLicense(new Byte[]{10, 20, 30, 40});
        testEntity.setBankDetails(new BankDetailsEntity());

        return testEntity;
    }

    private LicenseDto createTestDto() {
        var testDto = new LicenseDto();

        testDto.setId(1L);
        testDto.setPhotoLicense(new Byte[]{10, 20, 30, 40});
        testDto.setBankDetails(new BankDetailsDto());

        return testDto;
    }

    @BeforeEach
    void setup() {
        testEntity = createTestEntity();
        testDto = createTestDto();
    }

    @Test
    @DisplayName("маппинг в энтити")
    void toEntityTest() {
        var actualEntity = licenseMapper.toEntity(testDto);

        assertThat(actualEntity)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(testEntity);
    }

    @Test
    @DisplayName("маппинг в энтити, на вход подан null")
    void toEntityNullTest() {
        var actualEntity = licenseMapper.toEntity(null);

        assertThat(actualEntity)
                .as("Ожидался null")
                .isNull();
    }

    @Test
    @DisplayName("маппинг в дто")
    void toDtoTest() {
        var actualDto = licenseMapper.toDto(testEntity);

        assertThat(actualDto)
                .usingRecursiveComparison()
                .isEqualTo(testDto);
    }

    @Test
    @DisplayName("маппинг в дто, на вход подан null")
    void toDtoNullTest() {
        var expectedDto = licenseMapper.toDto(null);

        assertThat(expectedDto)
                .as("Ожидался null")
                .isNull();
    }

    @Test
    @DisplayName("слияние в энтити")
    void mergeToEntityTest() {
        var mergedEntity = licenseMapper.mergeToEntity(testDto, testEntity);

        assertThat(mergedEntity)
                .usingRecursiveComparison()
                .isEqualTo(testEntity);
    }

    @Test
    @DisplayName("слияние в энтити, на вход подан null")
    void mergeToEntityNullTest() {
        var mergedEntity = licenseMapper.mergeToEntity(null, testEntity);

        assertThat(mergedEntity).isEqualTo(testEntity);
    }

    @Test
    @DisplayName("маппинг в список дто")
    void toDtoListTest() {
        List<LicenseEntity> entityList = new ArrayList<>();

        LicenseEntity entity2 = createTestEntity();
        entity2.setId(2L);

        entityList.add(testEntity);
        entityList.add(entity2);

        List<LicenseDto> dtoList = licenseMapper.toDtoList(entityList);

        assertThat(dtoList)
                .as("Результат маппинга не должен быть null")
                .isNotNull()
                .as("Размеры списков не совпадают")
                .hasSameSizeAs(entityList);
    }

    @Test
    @DisplayName("маппинг в список дто, на вход подан null")
    void nullEntityListToDtoListTest() {
        List<LicenseDto> dtoList = licenseMapper.toDtoList(null);

        assertThat(dtoList)
                .as("Ожидался null, но фактический результат: " + dtoList)
                .isNull();
    }
}