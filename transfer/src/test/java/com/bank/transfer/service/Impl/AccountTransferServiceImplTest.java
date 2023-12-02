package com.bank.transfer.service.Impl;

import com.bank.transfer.dto.AccountTransferDto;
import com.bank.transfer.entity.AccountTransferEntity;
import com.bank.transfer.mapper.AccountTransferMapper;
import com.bank.transfer.repository.AccountTransferRepository;
import org.hibernate.SessionException;
import org.junit.jupiter.api.Assertions;
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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AccountTransferServiceImplTest {
    private final static String MESSAGE = "Не найден перевод по номеру счета с ID ";
    @Mock
    private AccountTransferRepository repository;
    @Mock
    private AccountTransferMapper mapper;
    @InjectMocks
    private AccountTransferServiceImpl accountTransferService;

    @Test
    @DisplayName("Получение списка переводов по номеру счёта по списку id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        AccountTransferEntity entity1 = getEntity1();
        AccountTransferEntity entity2 = getEntity2();
        AccountTransferDto dto1 = getDto1();
        AccountTransferDto dto2 = getDto2();

        doReturn(Optional.of(entity1)).when(repository).findById(entity1.getId());
        doReturn(Optional.of(entity2)).when(repository).findById(entity2.getId());
        doReturn(dto1).when(mapper).toDto(entity1);
        doReturn(dto2).when(mapper).toDto(entity2);

        List<AccountTransferDto> result = accountTransferService.findAllById(ids);

        assertAll(
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(dto1).isEqualTo(result.get(0)),
                () -> assertThat(dto2).isEqualTo(result.get(1)),
                () -> verify(repository, times(2)).findById(anyLong()),
                () -> verify(mapper, times(2)).toDto(any(AccountTransferEntity.class))
        );
    }

    @Test
    @DisplayName("Получение списка переводов по номеру счёта по списку id, негативный сценарий")
    void findAllByIdNegativeTest() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        when(repository.findById(anyLong())).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> accountTransferService.findAllById(ids));
        verify(repository).findById(anyLong());
        verify(mapper, never()).toDto(any(AccountTransferEntity.class));
    }

    @Test
    @DisplayName("Получение перевода по номеру счёта по id, позитивный сценарий")
    void findByIdPositiveTest() {
        Long id = 1L;
        AccountTransferEntity entity = getEntity1();
        AccountTransferDto dto = getDto1();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        AccountTransferDto result = accountTransferService.findById(id);

        assertAll(
                () -> assertThat(dto).isEqualTo(result),
                () -> verify(repository).findById(id),
                () -> verify(mapper).toDto(entity)
        );
    }

    @Test
    @DisplayName("Получение перевода по номеру счёта по id, негативный сценарий")
    void findByIdNegativeTest() {
        Long id = 1L;
        when(repository.findById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> accountTransferService.findById(id));
        verify(repository).findById(id);
        verify(mapper, never()).toDto(any(AccountTransferEntity.class));
    }

    @Test
    @DisplayName("Проверка выброса EntityNotFoundException при вызове repository.findById, негативный сценарий")
    void throwEntityNotFoundExceptionTest() {
        Long accountId = 1L;
        doThrow(new EntityNotFoundException(MESSAGE + accountId)).when(repository).findById(accountId);

        assertAll(
                () -> assertThrows(EntityNotFoundException.class,
                        () -> accountTransferService.findById(accountId)),
                () -> verify(repository, Mockito.times(1)).findById(accountId),
                () -> verify(mapper, Mockito.never()).toDto(Mockito.any())
        );
    }

    @Test
    @DisplayName("Создание перевода по номеру счёта, позитивный сценарий")
    void savePositiveTest() {
        AccountTransferDto dto = getDto1();
        AccountTransferEntity entity = getEntity1();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        AccountTransferDto result = accountTransferService.save(dto);

        assertAll(
                () -> assertThat(dto).isEqualTo(result),
                () -> verify(mapper).toEntity(dto),
                () -> verify(repository).save(entity),
                () -> verify(mapper).toDto(entity)
        );
    }

    @Test
    @DisplayName("Создание перевода по номеру счёта, негативный сценарий")
    void saveNegativeTest() {
        AccountTransferDto dto = getDto1();
        AccountTransferEntity entity = getEntity1();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenThrow(SessionException.class);

        assertThrows(SessionException.class, () -> accountTransferService.save(dto));
        verify(mapper).toEntity(dto);
        verify(repository).save(entity);
        verify(mapper, never()).toDto(any(AccountTransferEntity.class));
    }

    @Test
    @DisplayName("Обновление перевода по номеру счёта по id, позитивный сценарий")
    void updatePositiveTest() {
        Long accountId = 1L;
        Long notFoundEntityWithId = 2L;
        AccountTransferEntity entity1 = getEntity1();
        AccountTransferEntity entity12 = getEntity12();
        AccountTransferDto dto2 = getDto2();
        AccountTransferDto dto12 = getDto12();

        doReturn(Optional.of(entity1)).when(repository).findById(accountId);
        doReturn(entity12).when(mapper).mergeToEntity(dto2, entity1);
        doReturn(dto12).when(mapper).toDto(entity12);
        doReturn(entity12).when(repository).save(entity12);
        doThrow(EntityNotFoundException.class).when(repository).findById(notFoundEntityWithId);

        AccountTransferDto result = accountTransferService.update(accountId, dto2);

        assertAll(
                () -> assertThat(result).isEqualTo(dto12),
                () -> assertThrows(EntityNotFoundException.class,
                        () -> accountTransferService.update(notFoundEntityWithId, dto2)),
                () -> verify(repository).findById(accountId),
                () -> verify(mapper).mergeToEntity(dto2, entity1),
                () -> verify(mapper).toDto(entity12)
        );
    }

    @Test
    @DisplayName("Обновление перевода по номеру счёта по id, негативный сценарий")
    void updateNegativeTest() {
        Long accountId = 1L;
        AccountTransferEntity entity1 = getEntity1();
        AccountTransferDto dto2 = getDto2();

        when(repository.findById(accountId)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> accountTransferService.update(accountId, dto2));
        verify(repository).findById(accountId);
        verify(mapper, never()).mergeToEntity(dto2, entity1);
        verify(mapper, never()).toDto(any(AccountTransferEntity.class));
    }

    private AccountTransferEntity getEntity1() {
        return new AccountTransferEntity(1L,
                2345L, BigDecimal.valueOf(9999), "asd", 5L);
    }

    private AccountTransferEntity getEntity2() {
        return new AccountTransferEntity(2L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }

    private AccountTransferDto getDto1() {
        return new AccountTransferDto(1L,
                2345L, BigDecimal.valueOf(9999), "asd", 5L);
    }

    private AccountTransferDto getDto2() {
        return new AccountTransferDto(2L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }

    private AccountTransferEntity getEntity12() {
        return new AccountTransferEntity(1L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }

    private AccountTransferDto getDto12() {
        return new AccountTransferDto(1L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }
}