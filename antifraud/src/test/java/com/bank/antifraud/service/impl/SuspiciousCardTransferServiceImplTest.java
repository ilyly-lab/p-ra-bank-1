package com.bank.antifraud.service.impl;

import com.bank.antifraud.dto.SuspiciousCardTransferDto;
import com.bank.antifraud.entity.SuspiciousCardTransferEntity;
import com.bank.antifraud.mappers.SuspiciousCardTransferMapper;
import com.bank.antifraud.repository.SuspiciousCardTransferRepository;
import com.bank.antifraud.service.common.ExceptionReturner;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class SuspiciousCardTransferServiceImplTest {

    @Mock
    SuspiciousCardTransferMapper mapper;
    @Mock
    SuspiciousCardTransferRepository repository;
    @Mock
    ExceptionReturner returner;
    @InjectMocks
    SuspiciousCardTransferServiceImpl suspiciousCardTransferService;

    @Test
    @DisplayName("сохранение SuspiciousCardTransfer это подозрительные переводы по номеру карты, позитивный сценарий")
    void savePositiveTest() {
        SuspiciousCardTransferDto expectedResult = new SuspiciousCardTransferDto(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousCardTransferEntity transferEntity = new SuspiciousCardTransferEntity(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        when(mapper.toEntity(expectedResult)).thenReturn(transferEntity);
        when(repository.save(transferEntity)).thenReturn(transferEntity);
        when(mapper.toDto(transferEntity)).thenReturn(expectedResult);

        SuspiciousCardTransferDto actualResult = suspiciousCardTransferService.save(expectedResult);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("поиск SuspiciousCardTransfer это подозрительные переводы по номеру карты по id, позитивный сценарий")
    void findByIdPositiveTest() {
        SuspiciousCardTransferEntity transferEntity = new SuspiciousCardTransferEntity(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousCardTransferDto transferDto = new SuspiciousCardTransferDto(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        doReturn(Optional.of(transferEntity)).when(repository).findById(transferEntity.getId());
        doReturn(transferDto).when(mapper).toDto(transferEntity);

        SuspiciousCardTransferDto actualResult = suspiciousCardTransferService.findById(transferEntity.getId());

        assertEquals(actualResult, transferDto);
    }

    @Test
    @DisplayName("поиск SuspiciousCardTransfer это подозрительные переводы по номеру карты по id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        when(returner.getEntityNotFoundException("SuspiciousCardTransfer по данному id не существует"))
                .thenReturn(new EntityNotFoundException("SuspiciousCardTransfer по данному id не существует"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> suspiciousCardTransferService.findById(1L));
        assertEquals("SuspiciousCardTransfer по данному id не существует", exception.getMessage());
    }

    @Test
    @DisplayName("обновление SuspiciousCardTransfer это подозрительные переводы по номеру карты, позитивный сценарий")
    void updatePositiveTest() {
        Long id = 1L;
        SuspiciousCardTransferDto expectedResult = new SuspiciousCardTransferDto(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousCardTransferEntity transferEntity = new SuspiciousCardTransferEntity(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        when(repository.findById(id)).thenReturn(Optional.of(transferEntity));
        when(mapper.mergeToEntity(expectedResult, transferEntity)).thenReturn(transferEntity);
        when(repository.save(transferEntity)).thenReturn(transferEntity);
        when(mapper.toDto(transferEntity)).thenReturn(expectedResult);

        SuspiciousCardTransferDto actualResult = suspiciousCardTransferService.update(id, expectedResult);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("поиск списка SuspiciousCardTransfer это подозрительные переводы по номеру карты по id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        List<Long> ids = List.of(1L, 2L, 3L);
        SuspiciousCardTransferEntity entity1 = new SuspiciousCardTransferEntity(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousCardTransferEntity entity2 = new SuspiciousCardTransferEntity(2L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousCardTransferEntity entity3 = new SuspiciousCardTransferEntity(3L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        when(repository.findById(1L)).thenReturn(Optional.of(entity1));
        when(repository.findById(2L)).thenReturn(Optional.of(entity2));
        when(repository.findById(3L)).thenReturn(Optional.of(entity3));

        List<SuspiciousCardTransferDto> expectedResult = new ArrayList<>(Arrays.asList(
                new SuspiciousCardTransferDto(1L, 13L, false,
                        false, "blockedReason", "suspiciousReason"),
                new SuspiciousCardTransferDto(2L, 13L, false,
                        false, "blockedReason", "suspiciousReason"),
                new SuspiciousCardTransferDto(3L, 13L, false,
                        false, "blockedReason", "suspiciousReason")
        ));

        when(mapper.toListDto(Arrays.asList(entity1, entity2, entity3))).thenReturn(expectedResult);
        List<SuspiciousCardTransferDto> actualResult = suspiciousCardTransferService.findAllById(ids);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("поиск списка SuspiciousCardTransfer это подозрительные переводы по номеру карты по id, негативный сценарий")
    void findAllByIdNegativeTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        when(returner.getEntityNotFoundException("SuspiciousCardTransfer по данному id не существует"))
                .thenReturn(new EntityNotFoundException("SuspiciousCardTransfer по данному id не существует"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> suspiciousCardTransferService.findById(1L));

        assertEquals("SuspiciousCardTransfer по данному id не существует", exception.getMessage());
    }
}