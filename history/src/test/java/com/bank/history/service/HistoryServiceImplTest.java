package com.bank.history.service;

import com.bank.history.dto.HistoryDto;
import com.bank.history.entity.HistoryEntity;
import com.bank.history.mapper.HistoryMapper;
import com.bank.history.repository.HistoryRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class HistoryServiceImplTest {
    @Mock
    HistoryRepository historyRepository;
    @Mock
    HistoryMapper historyMapper;
    @InjectMocks
    HistoryServiceImpl historyService;

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void readById() {
        HistoryEntity historyEntity = getHistoryEntity();
        HistoryDto auditDto = getHistoryDto();

        doReturn(Optional.of(historyEntity)).when(historyRepository).findById(historyEntity.getId());
        doReturn(auditDto).when(historyMapper).toDto(historyEntity);

        HistoryDto actualResult = historyService.readById(historyEntity.getId());

        assertEquals(actualResult, auditDto);
    }

    @Test
    @DisplayName("поиск по id, негатвный сценарий")
    void readByIdFalse() {
        long nonExistingId = 100;

        doReturn(Optional.empty()).when(historyRepository).findById(nonExistingId);

        assertThrows(EntityNotFoundException.class, () -> {
                    historyService.readById(nonExistingId);
                }
        );

    }

    @Test
    @DisplayName("поиск списка-HistoryEntity по списку id, позитивный сценарий")
    void readAllById() {
        List<Long> ids = List.of(111L, 222L, 333L);

        List<HistoryEntity> historyEntityList = getListEntity();

        when(historyRepository.findAllById(ids)).thenReturn(historyEntityList);

        List<HistoryDto> expectedResult = getListDTO();

        when(historyMapper.toListDto(historyEntityList)).thenReturn(expectedResult);

        List<HistoryDto> actualResult = historyService.readAllById(ids);
        assertEquals(expectedResult, actualResult);
    }
    @Test
    @DisplayName("поиск списка-HistoryEntity по списку id, негативный сценарий")
    void readAllByIdFalse() {
        List<Long> ids = List.of(111L, 222L, 333L);

        when(historyRepository.findAllById(ids)).thenReturn(new ArrayList<>());

        assertThrows(EntityNotFoundException.class, () -> {
                    historyService.readAllById(ids);
                }
        );
    }

    @Test
    @DisplayName("сохранение сущности, позитивный сценарий")
    void create() {
        HistoryDto expectedResult = getHistoryDto();

        HistoryEntity detailsEntity = getHistoryEntity();

        when(historyMapper.toEntity(expectedResult)).thenReturn(detailsEntity);

        when(historyRepository.save(detailsEntity)).thenReturn(detailsEntity);

        when(historyMapper.toDto(detailsEntity)).thenReturn(expectedResult);

        HistoryDto actualResult = historyService.create(expectedResult);

        assertEquals(expectedResult, actualResult);
    }
    @Test
    @DisplayName("сохранение сущности, негативный сценарий")
    void createFalse() {
        HistoryDto expectedResult = getHistoryDto();

        HistoryEntity detailsEntity = getHistoryEntity();

        when(historyMapper.toEntity(expectedResult)).thenReturn(detailsEntity);

        when(historyRepository.save(detailsEntity)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
                    historyService.create(expectedResult);
                }
        );

        verify(historyRepository, times(1)).save(detailsEntity);
    }

    @Test
    @DisplayName("обновление сущности по id, позитивный сценарий")
    void update() {
        Long id = 10L;
        HistoryDto expectedResult = getHistoryDto();
        HistoryEntity detailsEntity = getHistoryEntity();

        when(historyRepository.findById(id)).thenReturn(Optional.of(detailsEntity));
        when(historyMapper.mergeToEntity(expectedResult, detailsEntity)).thenReturn(detailsEntity);
        when(historyRepository.save(detailsEntity)).thenReturn(detailsEntity);
        when(historyMapper.toDto(detailsEntity)).thenReturn(expectedResult);

        HistoryDto actualResult = historyService.update(id, expectedResult);

        assertEquals(expectedResult, actualResult);
    }
    @Test
    @DisplayName("обновление сущности по id, негативный сценарий")
    void updateFalse() {
        Long id = 10L;
        HistoryDto expectedResult = getHistoryDto();

        when(historyRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
                    historyService.update(id, expectedResult);
                }
        );
    }


    List<HistoryDto> getListDTO() {
        HistoryDto entity11 = new HistoryDto(111L,111L,111L
                ,111L,111L,111L,111L);

        HistoryDto entity22 = new HistoryDto(222L,111L,111L
                ,111L,111L,111L,111L);

        HistoryDto entity33 = new HistoryDto(333L,111L,111L
                ,111L,111L,111L,111L);

        List<HistoryDto> dtoList = new ArrayList<>();

        dtoList.add(entity11);
        dtoList.add(entity22);
        dtoList.add(entity33);

        return dtoList;

    }
    List<HistoryEntity> getListEntity() {
        HistoryEntity entity1 = new HistoryEntity(111L,111L,111L
                ,111L,111L,111L,111L);

        HistoryEntity entity2 = new HistoryEntity(222L,111L,111L
                ,111L,111L,111L,111L);

        HistoryEntity entity3 = new HistoryEntity(333L,111L,111L
                ,111L,111L,111L,111L);

        List<HistoryEntity> listEntyti = new ArrayList<>();

        listEntyti.add(entity1);
        listEntyti.add(entity2);
        listEntyti.add(entity3);

        return listEntyti;
    }

    HistoryEntity getHistoryEntity() {
        HistoryEntity historyEntity = new HistoryEntity(111L,111L,111L
                ,111L,111L,111L,111L);
        return historyEntity;
    }
    HistoryDto getHistoryDto() {
    HistoryDto historyDto = new HistoryDto(111L,111L,111L
            ,111L,111L,111L,111L);
    return historyDto;
    }
}