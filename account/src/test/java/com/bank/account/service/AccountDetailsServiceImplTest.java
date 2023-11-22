package com.bank.account.service;

import com.bank.account.dto.AccountDetailsDto;
import com.bank.account.entity.AccountDetailsEntity;
import com.bank.account.mapper.AccountDetailsMapper;
import com.bank.account.repository.AccountDetailsRepository;
import com.bank.account.service.common.ExceptionReturner;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class AccountDetailsServiceImplTest {

    @Mock
    AccountDetailsMapper mapper;
    @Mock
    AccountDetailsRepository repository;
    @Mock
    ExceptionReturner exceptionReturner;
    @InjectMocks
    AccountDetailsServiceImpl accountDetailsService;

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AccountDetailsEntity detailsEntity = getDetailsEntity();
        AccountDetailsDto detailsDto = getDetailsDto();
        doReturn(Optional.of(detailsEntity)).when(repository).findById(detailsEntity.getId());
        doReturn(detailsDto).when(mapper).toDto(detailsEntity);

        AccountDetailsDto actualResult = accountDetailsService.findById(detailsEntity.getId());

        assertEquals(actualResult, detailsDto);
    }

    @Test
    @DisplayName("поиск по id, негативный сценарий")
    void findByNonExistIdNegativeNest() {
        Long invalidId = 1L;

        when(repository.findById(invalidId)).thenReturn(Optional.empty());
        when(exceptionReturner.getEntityNotFoundException("Не существующий id = " + invalidId))
                .thenReturn(new EntityNotFoundException("Не существующий id = " + invalidId));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accountDetailsService.findById(invalidId)
        );
        assertEquals("Не существующий id = " + invalidId, exception.getMessage());

        verify(mapper, never()).toDto(any());
    }

    @Test
    @DisplayName("поиск списка AccountDetailsEntity по списку id, позитивный сценарий")
    void findAllByIdsPositiveTest() {
        List<Long> ids = List.of(1L, 2L, 3L);

        AccountDetailsEntity entity1 = new AccountDetailsEntity(1L, 230L, 123L, 790L,
                new BigDecimal("109"), false, 290L);
        AccountDetailsEntity entity2 = new AccountDetailsEntity(2L, 230L, 123L, 790L,
                new BigDecimal("109"), false, 290L);
        AccountDetailsEntity entity3 = new AccountDetailsEntity(3L, 230L, 123L, 790L,
                new BigDecimal("109"), false, 290L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity1));
        when(repository.findById(2L)).thenReturn(Optional.of(entity2));
        when(repository.findById(3L)).thenReturn(Optional.of(entity3));

        List<AccountDetailsDto> expectedResult = getDetailsDtoList();

        when(mapper.toDtoList(Arrays.asList(entity1, entity2, entity3))).thenReturn(expectedResult);

        List<AccountDetailsDto> actualResult = accountDetailsService.findAllById(ids);
        assertEquals(expectedResult, actualResult);

    }

    @Test
    @DisplayName("поиск списка AccountDetailsEntity по списку id, негативный сценарий")
    void findAllByNonExistIdsNegativeNest() {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        Long invalidId = 1L;

        when(repository.findById(invalidId)).thenReturn(Optional.empty());
        when(exceptionReturner.getEntityNotFoundException("Не существующий id = " + invalidId))
                .thenReturn(new EntityNotFoundException("Не существующий id = " + invalidId));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accountDetailsService.findById(invalidId)
        );
        assertEquals("Не существующий id = " + invalidId, exception.getMessage());

        verify(mapper, never()).toDto(any());
    }

    @Test
    @DisplayName("сохранение AccountDetailsEntity, позитивный сценарий")
    void savePositiveTest() {
        AccountDetailsDto expectedResult = getDetailsDto();
        AccountDetailsEntity detailsEntity = getDetailsEntity();
        when(mapper.toEntity(expectedResult)).thenReturn(detailsEntity);
        when(repository.save(detailsEntity)).thenReturn(detailsEntity);
        when(mapper.toDto(detailsEntity)).thenReturn(expectedResult);

        AccountDetailsDto actualResult = accountDetailsService.save(expectedResult);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    @DisplayName("обновление AccountDetailsEntity, позитивный сценарий")
    void updatePositiveTest() {
        Long id = 10L;
        AccountDetailsDto expectedResult = getDetailsDto();
        AccountDetailsEntity detailsEntity = getDetailsEntity();

        when(repository.findById(id)).thenReturn(Optional.of(detailsEntity));
        when(mapper.mergeToEntity(detailsEntity, expectedResult)).thenReturn(detailsEntity);
        when(repository.save(detailsEntity)).thenReturn(detailsEntity);
        when(mapper.toDto(detailsEntity)).thenReturn(expectedResult);

        AccountDetailsDto actualResult = accountDetailsService.update(id, expectedResult);

        assertEquals(expectedResult, actualResult);
    }

    private List<AccountDetailsDto> getDetailsDtoList() {
        return new ArrayList<>(Arrays.asList(
                new AccountDetailsDto(1L, 230L, 123L, 790L,
                        new BigDecimal("109"), false, 290L),
                new AccountDetailsDto(2L, 230L, 123L, 790L,
                        new BigDecimal("109"), false, 290L),
                new AccountDetailsDto(3L, 230L, 123L, 790L,
                        new BigDecimal("109"), false, 290L)
        ));
    }

    private AccountDetailsDto getDetailsDto() {
        return new AccountDetailsDto(10L, 230L, 123L, 790L,
                new BigDecimal("109"), false, 290L);
    }

    private AccountDetailsEntity getDetailsEntity() {
        return new AccountDetailsEntity(10L, 230L, 123L, 790L,
                new BigDecimal("109"), false, 290L);
    }

}