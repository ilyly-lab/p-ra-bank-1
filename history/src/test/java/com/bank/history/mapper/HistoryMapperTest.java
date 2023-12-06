package com.bank.history.mapper;

import com.bank.history.dto.HistoryDto;
import com.bank.history.entity.HistoryEntity;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class HistoryMapperTest {

    private final HistoryMapper mapper = Mappers.getMapper(HistoryMapper.class);

    @Test
    @DisplayName("преобразование сущности дто в сущность энтити, позитивный сценарий")
    void toEntity() {
        HistoryEntity result = mapper.toEntity(dto);
        assertNull(result.getId());
        result.setId(1L);
        assertEquals(result,entity);
    }
    @Test
    @DisplayName("преобразование сущности дто в сущность энтити, на вход передаем налл")
    void toEntityNull() {
        HistoryEntity result = mapper.toEntity(null);
        assertNull(result);
    }

    @Test
    @DisplayName("преобразование сущности энтити в сущность дто, позитивный сценарий")
    void toDto() {
        HistoryDto result = mapper.toDto(entity);
        assertEquals(result, dto);
    }
    @Test
    @DisplayName("преобразование сущности энтити в сущность дто, на вход подаем налл")
    void toDtoNull() {
        HistoryDto result = mapper.toDto(null);
        assertNull(result);
    }

    @Test
    @DisplayName("слияние сущностей дто и энтити, позитивный сценарий")
    void mergeToEntity() {
        HistoryEntity result = mapper.mergeToEntity(dto,entity);
        assertEquals(result,entity);
    }
    @Test
    @DisplayName("слияние сущностей дто и энтити, вместо дто подаем налл")
    void mergeToEntityNull() {
        HistoryEntity result = mapper.mergeToEntity(null,entity);
        assertEquals(result,entity);
    }

    @Test
    @DisplayName("преобразуем лист-энтити в лист-дто, позитивный сценарий")
    void toListDto() {
        List<HistoryDto> result = mapper.toListDto(getEntityList());
        assertEquals(result, getDtoList());

    }
    @Test
    @DisplayName("преобразуем лист-ентити в лист-дто, на вход подаем налл")
    void toListDtoNull() {
        List<HistoryDto> result = mapper.toListDto(null);
        assertNull(result);
    }

    HistoryEntity entity = new HistoryEntity(1L,2L,3L,4L,
            5L,6L,7L);
    HistoryDto dto = new HistoryDto(1L,2L,3L,4L,5L,
            6L,7L);

    List<HistoryDto> getDtoList() {
        List<HistoryDto> dtoList = new ArrayList<>();
        dtoList.add(new HistoryDto(1L,2L,3L,4L,5L,
                6L,7L));
        dtoList.add(new HistoryDto(1L,2L,3L,4L,5L,
                6L,7L));
        dtoList.add(new HistoryDto(1L,2L,3L,4L,5L,
                6L,7L));
        return dtoList;
    }
    List<HistoryEntity> getEntityList() {
        List<HistoryEntity> entityList = new ArrayList<>();
        entityList.add(new HistoryEntity(1L,2L,3L,4L,
                5L,6L,7L));
        entityList.add(new HistoryEntity(1L,2L,3L,4L,
                5L,6L,7L));
        entityList.add(new HistoryEntity(1L,2L,3L,4L,
                5L,6L,7L));
        return entityList;
    }
}