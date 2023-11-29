package com.bank.antifraud.service.impl;

import com.bank.antifraud.dto.SuspiciousAccountTransferDto;
import com.bank.antifraud.entity.SuspiciousAccountTransferEntity;
import com.bank.antifraud.mappers.SuspiciousAccountTransferMapper;
import com.bank.antifraud.repository.SuspiciousAccountTransferRepository;
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
class SuspiciousAccountTransferServiceImplTest {

    @Mock
    SuspiciousAccountTransferMapper mapper;
    @Mock
    SuspiciousAccountTransferRepository repository;
    @Mock
    ExceptionReturner returner;
    @InjectMocks
    SuspiciousAccountTransferServiceImpl suspiciousAccountTransferService;

    @Test
    @DisplayName("сохранение SuspiciousAccountTransfer это подозрительные переводы по номеру счёта, позитивный сценарий")
    void savePositiveTest() {
        SuspiciousAccountTransferDto expectedResult = new SuspiciousAccountTransferDto(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousAccountTransferEntity transferEntity = new SuspiciousAccountTransferEntity(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        when(mapper.toEntity(expectedResult)).thenReturn(transferEntity);
        when(repository.save(transferEntity)).thenReturn(transferEntity);
        when(mapper.toDto(transferEntity)).thenReturn(expectedResult);

        SuspiciousAccountTransferDto actualResult = suspiciousAccountTransferService.save(expectedResult);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("поиск SuspiciousAccountTransfer это подозрительные переводы по номеру счёта по id, позитивный сценарий")
    void findByIdPositiveTest() {
        SuspiciousAccountTransferEntity transferEntity = new SuspiciousAccountTransferEntity(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousAccountTransferDto transferDto = new SuspiciousAccountTransferDto(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        doReturn(Optional.of(transferEntity)).when(repository).findById(transferEntity.getId());
        doReturn(transferDto).when(mapper).toDto(transferEntity);

        SuspiciousAccountTransferDto actualResult = suspiciousAccountTransferService.findById(transferEntity.getId());

        assertEquals(actualResult, transferDto);
    }

    @Test
    @DisplayName("поиск SuspiciousAccountTransfer это подозрительные переводы по номеру счёта по id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        when(returner.getEntityNotFoundException("SuspiciousAccountTransfer по данному id не существует"))
                .thenReturn(new EntityNotFoundException("SuspiciousAccountTransfer по данному id не существует"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> suspiciousAccountTransferService.findById(1L));
        assertEquals("SuspiciousAccountTransfer по данному id не существует", exception.getMessage());
    }

    @Test
    @DisplayName("обновление SSuspiciousAccountTransfer это подозрительные переводы по номеру счёта, позитивный сценарий")
    void updatePositiveTest() {
        Long id = 1L;
        SuspiciousAccountTransferDto expectedResult = new SuspiciousAccountTransferDto(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousAccountTransferEntity transferEntity = new SuspiciousAccountTransferEntity(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        when(repository.findById(id)).thenReturn(Optional.of(transferEntity));
        when(mapper.mergeToEntity(expectedResult, transferEntity)).thenReturn(transferEntity);
        when(repository.save(transferEntity)).thenReturn(transferEntity);
        when(mapper.toDto(transferEntity)).thenReturn(expectedResult);

        SuspiciousAccountTransferDto actualResult = suspiciousAccountTransferService.update(id, expectedResult);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("поиск списка SuspiciousAccountTransfer это подозрительные переводы по номеру счёта по id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        List<Long> ids = List.of(1L, 2L, 3L);
        SuspiciousAccountTransferEntity entity1 = new SuspiciousAccountTransferEntity(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousAccountTransferEntity entity2 = new SuspiciousAccountTransferEntity(2L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousAccountTransferEntity entity3 = new SuspiciousAccountTransferEntity(3L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        when(repository.findById(1L)).thenReturn(Optional.of(entity1));
        when(repository.findById(2L)).thenReturn(Optional.of(entity2));
        when(repository.findById(3L)).thenReturn(Optional.of(entity3));

        List<SuspiciousAccountTransferDto> expectedResult = new ArrayList<>(Arrays.asList(
                new SuspiciousAccountTransferDto(1L, 13L, false,
                        false, "blockedReason", "suspiciousReason"),
                new SuspiciousAccountTransferDto(2L, 13L, false,
                        false, "blockedReason", "suspiciousReason"),
                new SuspiciousAccountTransferDto(3L, 13L, false,
                        false, "blockedReason", "suspiciousReason")
        ));

        when(mapper.toListDto(Arrays.asList(entity1, entity2, entity3))).thenReturn(expectedResult);
        List<SuspiciousAccountTransferDto> actualResult = suspiciousAccountTransferService.findAllById(ids);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("поиск списка SuspiciousAccountTransfer это подозрительные переводы по номеру счёта по id, негативный сценарий")
    void findAllByIdNegativeTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        when(returner.getEntityNotFoundException("SuspiciousAccountTransfer по данному id не существует"))
                .thenReturn(new EntityNotFoundException("SuspiciousAccountTransfer по данному id не существует"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> suspiciousAccountTransferService.findById(1L));

        assertEquals("SuspiciousAccountTransfer по данному id не существует", exception.getMessage());
    }
}