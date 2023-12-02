package com.bank.transfer.service.Impl;

import com.bank.transfer.dto.CardTransferDto;
import com.bank.transfer.entity.CardTransferEntity;
import com.bank.transfer.mapper.CardTransferMapper;
import com.bank.transfer.repository.CardTransferRepository;
import org.hibernate.SessionException;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class CardTransferServiceImplTest {
    private final static String MESSAGE = "Не найден перевод по номеру карты с ID ";
    @Mock
    CardTransferRepository repository;
    @Mock
    CardTransferMapper mapper;
    @InjectMocks
    CardTransferServiceImpl cardTransferService;

    @Test
    @DisplayName("Получение списка переводов по номеру карты по списку id, позитивный сценарий")
    void findAllByIdPositiveTest() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        CardTransferEntity entity1 = getEntity1();
        CardTransferEntity entity2 = getEntity2();
        CardTransferDto dto1 = getDto1();
        CardTransferDto dto2 = getDto2();

        doReturn(Optional.of(entity1)).when(repository).findById(entity1.getId());
        doReturn(Optional.of(entity2)).when(repository).findById(entity2.getId());
        doReturn(dto1).when(mapper).toDto(entity1);
        doReturn(dto2).when(mapper).toDto(entity2);

        List<CardTransferDto> result = cardTransferService.findAllById(ids);

        assertAll(
                () -> assertThat(result.size()).isEqualTo(2),
                () -> assertThat(dto1).isEqualTo(result.get(0)),
                () -> assertThat(dto2).isEqualTo(result.get(1)),
                () -> verify(repository, times(2)).findById(anyLong()),
                () -> verify(mapper, times(2)).toDto(any(CardTransferEntity.class))
        );
    }

    @Test
    @DisplayName("Получение списка переводов по номеру карты по списку id, негативный сценарий")
    void findAllByIdNegativeTest() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        doThrow(EntityNotFoundException.class).when(repository).findById(ids.get(0));

        assertThrows(EntityNotFoundException.class, () -> cardTransferService.findAllById(ids));
        verify(mapper, never()).toDto(any(CardTransferEntity.class));
    }


    @Test
    @DisplayName("Получение перевода по номеру карты по id, позитивный сценарий")
    void findByIdPositiveTest() {
        Long id = 1L;
        CardTransferEntity entity = getEntity1();
        CardTransferDto dto = getDto1();
        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        CardTransferDto result = cardTransferService.findById(id);

        assertAll(
                () -> assertThat(dto).isEqualTo(result),
                () -> verify(repository).findById(id),
                () -> verify(mapper).toDto(entity)
        );
    }

    @Test
    @DisplayName("Получение перевода по номеру карты по id, негативный сценарий")
    void findByIdNegativeTest() {
        Long id = 1L;

        when(repository.findById(id)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> cardTransferService.findById(id));
    }

    @Test
    @DisplayName("Проверка выброса EntityNotFoundException при вызове repository.findById, негативный сценарий")
    void throwEntityNotFoundExceptionTest() {
        Long accountId = 1L;
        doThrow(new EntityNotFoundException(MESSAGE + accountId)).when(repository).findById(accountId);

        assertAll(
                () -> assertThrows(EntityNotFoundException.class, () -> cardTransferService.findById(accountId)),
                () -> verify(repository, Mockito.times(1)).findById(accountId),
                () -> verify(mapper, Mockito.never()).toDto(Mockito.any())
        );
    }

    @Test
    @DisplayName("Создание перевода по номеру карты, позитивный сценарий")
    void savePositiveTest() {
        CardTransferDto dto = getDto1();
        CardTransferEntity entity = getEntity1();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        CardTransferDto result = cardTransferService.save(dto);

        assertAll(
                () -> assertThat(dto).isEqualTo(result),
                () -> verify(mapper).toEntity(dto),
                () -> verify(repository).save(entity),
                () -> verify(mapper).toDto(entity)
        );
    }

    @Test
    @DisplayName("Создание перевода по номеру карты, негативный сценарий")
    void saveNegativeTest() {
        CardTransferDto dto = getDto1();
        CardTransferEntity entity = getEntity1();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenThrow(SessionException.class);

        assertAll(
                () -> assertThrows(SessionException.class, () -> cardTransferService.save(dto)),
                () -> verify(mapper).toEntity(dto),
                () -> verify(repository).save(entity),
                () -> verify(mapper, never()).toDto(entity)
        );
    }

    @Test
    @DisplayName("Обновление перевода по номеру карты по id, позитивный сценарий с проверкой на null")
    void updatePositiveAndNullTest() {
        Long accountId = 1L;
        Long notFoundEntityWithId = 2L;
        CardTransferEntity entity1 = getEntity1();
        CardTransferEntity entity12 = getEntity12();
        CardTransferDto dto2 = getDto2();
        CardTransferDto dto12 = getDto12();

        doReturn(Optional.of(entity1)).when(repository).findById(accountId);
        doReturn(entity12).when(mapper).mergeToEntity(dto2, entity1);
        doReturn(dto12).when(mapper).toDto(entity12);
        doReturn(entity12).when(repository).save(entity12);
        doThrow(EntityNotFoundException.class).when(repository).findById(notFoundEntityWithId);

        CardTransferDto result = cardTransferService.update(accountId, dto2);

        assertAll(
                () -> assertThat(result).isEqualTo(dto12),
                () -> assertThrows(EntityNotFoundException.class,
                        () -> cardTransferService.update(notFoundEntityWithId, dto2)),
                () -> verify(repository).findById(accountId),
                () -> verify(mapper).mergeToEntity(dto2, entity1),
                () -> verify(mapper).toDto(entity12)
        );
    }

    private CardTransferEntity getEntity1() {
        return new CardTransferEntity(1L,
                2345L, BigDecimal.valueOf(9999), "asd", 5L);
    }

    private CardTransferEntity getEntity2() {
        return new CardTransferEntity(2L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }

    private CardTransferDto getDto1() {
        return new CardTransferDto(1L,
                2345L, BigDecimal.valueOf(9999), "asd", 5L);
    }

    private CardTransferDto getDto2() {
        return new CardTransferDto(2L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }

    private CardTransferEntity getEntity12() {
        return new CardTransferEntity(1L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }

    private CardTransferDto getDto12() {
        return new CardTransferDto(1L,
                23452L, BigDecimal.valueOf(7777), "mms", 2L);
    }

}