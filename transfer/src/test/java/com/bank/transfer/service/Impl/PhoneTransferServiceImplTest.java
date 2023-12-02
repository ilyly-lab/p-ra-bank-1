package com.bank.transfer.service.Impl;

import com.bank.transfer.dto.PhoneTransferDto;
import com.bank.transfer.entity.PhoneTransferEntity;
import com.bank.transfer.mapper.PhoneTransferMapper;
import com.bank.transfer.repository.PhoneTransferRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhoneTransferServiceImplTest {
    private final static String MESSAGE = "Не найден перевод по номеру телефона с ID ";
    @Mock
    PhoneTransferRepository repository;
    @Mock
    PhoneTransferMapper mapper;
    @InjectMocks
    PhoneTransferServiceImpl phoneTransferService;

    @Test
    @DisplayName("Получение списка переводов по номеру телефона по списку id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        PhoneTransferEntity entity1 = getEntity1();
        PhoneTransferEntity entity2 = getEntity2();
        PhoneTransferDto dto1 = getDto1();
        PhoneTransferDto dto2 = getDto2();

        doReturn(Optional.of(entity1)).when(repository).findById(entity1.getId());
        doReturn(Optional.of(entity2)).when(repository).findById(entity2.getId());
        doReturn(dto1).when(mapper).toDto(entity1);
        doReturn(dto2).when(mapper).toDto(entity2);

        List<PhoneTransferDto> result = phoneTransferService.findAllById(ids);

        assertAll(
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(dto1).isEqualTo(result.get(0)),
                () -> assertThat(dto2).isEqualTo(result.get(1)),
                () -> verify(repository, times(2)).findById(anyLong()),
                () -> verify(mapper, times(2)).toDto(any(PhoneTransferEntity.class))
        );
    }

    @Test
    @DisplayName("Получение списка переводов по номеру телефона по списку id, негативный сценарий")
    void findAllByIdNegativeTest() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        doThrow(EntityNotFoundException.class).when(repository).findById(1L);

        assertAll(
                () -> assertThrows(EntityNotFoundException.class, () -> phoneTransferService.findAllById(ids)),
                () -> verify(repository).findById(anyLong()),
                () -> verify(mapper, never()).toDto(any(PhoneTransferEntity.class))
        );
    }

    @Test
    @DisplayName("Получение перевода по номеру телефона по id, позитивный сценарий")
    void findByIdPositiveTest() {
        Long id = 1L;
        PhoneTransferEntity entity = getEntity1();
        PhoneTransferDto dto = getDto1();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        PhoneTransferDto result = phoneTransferService.findById(id);

        assertAll(
                () -> assertThat(dto).isEqualTo(result),
                () -> verify(repository).findById(id),
                () -> verify(mapper).toDto(entity)
        );
    }

    @Test
    @DisplayName("Получение перевода по номеру телефона по id, негативный сценарий")
    void findByIdNegativeTest() {
        Long id = 1L;
        when(repository.findById(id)).thenThrow(EntityNotFoundException.class);

        assertAll(
                () -> assertThrows(EntityNotFoundException.class, () -> phoneTransferService.findById(id)),
                () -> verify(repository).findById(id),
                () -> verify(mapper, never()).toDto(any())
        );
    }

    @Test
    @DisplayName("Проверка выброса EntityNotFoundException при вызове repository.findById, негативный сценарий")
    void throwEntityNotFoundExceptionTest() {
        Long accountId = 1L;
        doThrow(new EntityNotFoundException(MESSAGE + accountId)).when(repository).findById(accountId);

        assertAll(
                () -> assertThrows(EntityNotFoundException.class,
                        () -> phoneTransferService.findById(accountId)),
                () -> verify(repository, Mockito.times(1)).findById(accountId),
                () -> verify(mapper, Mockito.never()).toDto(Mockito.any())
        );
    }

    @Test
    @DisplayName("Создание перевода по номеру телефона, позитивный сценарий")
    void savePositiveTest() {
        PhoneTransferDto dto = getDto1();
        PhoneTransferEntity entity = getEntity1();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        PhoneTransferDto result = phoneTransferService.save(dto);

        assertAll(
                () -> assertThat(dto).isEqualTo(result),
                () -> verify(mapper).toEntity(dto),
                () -> verify(repository).save(entity),
                () -> verify(mapper).toDto(entity)
        );
    }

    @Test
    @DisplayName("Создание перевода по номеру телефона, негативный сценарий")
    void saveNegativeTest() {
        PhoneTransferDto dto = getDto1();
        PhoneTransferEntity entity = getEntity1();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenThrow(EntityNotFoundException.class);

        assertAll(
                () -> assertThrows(EntityNotFoundException.class, () -> phoneTransferService.save(dto)),
                () -> verify(mapper).toEntity(dto),
                () -> verify(repository).save(entity),
                () -> verify(mapper, never()).toDto(entity)
        );
    }

    @Test
    @DisplayName("Обновление перевода по номеру телефона по id, позитивный сценарий с проверкой на null")
    void updatePositiveAndNullTest() {
        Long accountId = 1L;
        Long notFoundEntityWithId = 2L;
        PhoneTransferEntity entity1 = getEntity1();
        PhoneTransferEntity entity12 = getEntity12();
        PhoneTransferDto dto2 = getDto2();
        PhoneTransferDto dto12 = getDto12();

        doReturn(Optional.of(entity1)).when(repository).findById(accountId);
        doReturn(entity12).when(mapper).mergeToEntity(dto2, entity1);
        doReturn(dto12).when(mapper).toDto(entity12);
        doReturn(entity12).when(repository).save(entity12);
        doThrow(EntityNotFoundException.class).when(repository).findById(notFoundEntityWithId);

        PhoneTransferDto result = phoneTransferService.update(accountId, dto2);

        assertAll(
                () -> assertThat(result).isEqualTo(dto12),
                () -> assertThrows(EntityNotFoundException.class,
                        () -> phoneTransferService.update(notFoundEntityWithId, dto2)),
                () -> verify(repository).findById(accountId),
                () -> verify(mapper).mergeToEntity(dto2, entity1),
                () -> verify(mapper).toDto(entity12)
        );
    }

    private PhoneTransferEntity getEntity1() {
        return new PhoneTransferEntity(1L,
                2345L, BigDecimal.valueOf(9999), "asd", 5L);
    }

    private PhoneTransferEntity getEntity2() {
        return new PhoneTransferEntity(2L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }

    private PhoneTransferDto getDto1() {
        return new PhoneTransferDto(1L,
                2345L, BigDecimal.valueOf(9999), "asd", 5L);
    }

    private PhoneTransferDto getDto2() {
        return new PhoneTransferDto(2L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }

    private PhoneTransferEntity getEntity12() {
        return new PhoneTransferEntity(1L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }

    private PhoneTransferDto getDto12() {
        return new PhoneTransferDto(1L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }
}