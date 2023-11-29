package com.bank.antifraud.service.impl;

import com.bank.antifraud.dto.SuspiciousPhoneTransferDto;
import com.bank.antifraud.entity.SuspiciousPhoneTransferEntity;
import com.bank.antifraud.mappers.SuspiciousPhoneTransferMapper;
import com.bank.antifraud.repository.SuspiciousPhoneTransferRepository;
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
class SuspiciousPhoneTransferServiceImplTest {

    @Mock
    SuspiciousPhoneTransferMapper mapper;
    @Mock
    SuspiciousPhoneTransferRepository repository;
    @Mock
    ExceptionReturner returner;
    @InjectMocks
    SuspiciousPhoneTransferServiceImpl suspiciousPhoneTransferService;

    @Test
    @DisplayName("сохранение SuspiciousPhoneTransfer это подозрительные переводы по номеру телефона, позитивный сценарий")
    void savePositiveTest() {
        SuspiciousPhoneTransferDto expectedResult = new SuspiciousPhoneTransferDto(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousPhoneTransferEntity transferEntity = new SuspiciousPhoneTransferEntity(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        when(mapper.toEntity(expectedResult)).thenReturn(transferEntity);
        when(repository.save(transferEntity)).thenReturn(transferEntity);
        when(mapper.toDto(transferEntity)).thenReturn(expectedResult);

        SuspiciousPhoneTransferDto actualResult = suspiciousPhoneTransferService.save(expectedResult);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("поиск SuspiciousPhoneTransfer это подозрительные переводы по номеру телефона по id, позитивный сценарий")
    void findByIdPositiveTest() {
        SuspiciousPhoneTransferEntity transferEntity = new SuspiciousPhoneTransferEntity(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousPhoneTransferDto transferDto = new SuspiciousPhoneTransferDto(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        doReturn(Optional.of(transferEntity)).when(repository).findById(transferEntity.getId());
        doReturn(transferDto).when(mapper).toDto(transferEntity);

        SuspiciousPhoneTransferDto actualResult = suspiciousPhoneTransferService.findById(transferEntity.getId());

        assertEquals(actualResult, transferDto);
    }

    @Test
    @DisplayName("поиск SuspiciousPhoneTransfer это подозрительные переводы по номеру телефона по id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        when(returner.getEntityNotFoundException("SuspiciousPhoneTransfer по данному id не существует"))
                .thenReturn(new EntityNotFoundException("SuspiciousPhoneTransfer по данному id не существует"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> suspiciousPhoneTransferService.findById(1L));
        assertEquals("SuspiciousPhoneTransfer по данному id не существует", exception.getMessage());
    }

    @Test
    @DisplayName("обновление SuspiciousPhoneTransfer это подозрительные переводы по номеру телефона, позитивный сценарий")
    void updatePositiveTest() {
        Long id = 1L;
        SuspiciousPhoneTransferDto expectedResult = new SuspiciousPhoneTransferDto(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousPhoneTransferEntity transferEntity = new SuspiciousPhoneTransferEntity(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        when(repository.findById(id)).thenReturn(Optional.of(transferEntity));
        when(mapper.mergeToEntity(expectedResult, transferEntity)).thenReturn(transferEntity);
        when(repository.save(transferEntity)).thenReturn(transferEntity);
        when(mapper.toDto(transferEntity)).thenReturn(expectedResult);

        SuspiciousPhoneTransferDto actualResult = suspiciousPhoneTransferService.update(id, expectedResult);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("поиск списка SuspiciousPhoneTransfer это подозрительные переводы по номеру телефона по id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        List<Long> ids = List.of(1L, 2L, 3L);
        SuspiciousPhoneTransferEntity entity1 = new SuspiciousPhoneTransferEntity(1L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousPhoneTransferEntity entity2 = new SuspiciousPhoneTransferEntity(2L,
                13L, false, false,
                "blockedReason", "suspiciousReason");
        SuspiciousPhoneTransferEntity entity3 = new SuspiciousPhoneTransferEntity(3L,
                13L, false, false,
                "blockedReason", "suspiciousReason");

        when(repository.findById(1L)).thenReturn(Optional.of(entity1));
        when(repository.findById(2L)).thenReturn(Optional.of(entity2));
        when(repository.findById(3L)).thenReturn(Optional.of(entity3));

        List<SuspiciousPhoneTransferDto> expectedResult = new ArrayList<>(Arrays.asList(
                new SuspiciousPhoneTransferDto(1L, 13L, false,
                        false, "blockedReason", "suspiciousReason"),
                new SuspiciousPhoneTransferDto(2L, 13L, false,
                        false, "blockedReason", "suspiciousReason"),
                new SuspiciousPhoneTransferDto(3L, 13L, false,
                        false, "blockedReason", "suspiciousReason")
        ));

        when(mapper.toListDto(Arrays.asList(entity1, entity2, entity3))).thenReturn(expectedResult);
        List<SuspiciousPhoneTransferDto> actualResult = suspiciousPhoneTransferService.findAllById(ids);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("поиск списка SuspiciousPhoneTransfer это подозрительные переводы по номеру телефона по id, негативный сценарий")
    void findAllByIdNegativeTest() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        when(returner.getEntityNotFoundException("SuspiciousPhoneTransfer по данному id не существует"))
                .thenReturn(new EntityNotFoundException("SuspiciousPhoneTransfer по данному id не существует"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> suspiciousPhoneTransferService.findById(1L));

        assertEquals("SuspiciousPhoneTransfer по данному id не существует", exception.getMessage());
    }
}