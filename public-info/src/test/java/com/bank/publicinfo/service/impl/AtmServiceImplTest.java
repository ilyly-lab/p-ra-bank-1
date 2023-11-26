package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.AtmDto;
import com.bank.publicinfo.entity.AtmEntity;
import com.bank.publicinfo.mapper.AtmMapper;
import com.bank.publicinfo.repository.AtmRepository;
import com.bank.publicinfo.util.EntityNotFoundSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class AtmServiceImplTest {
    private final static String MESSAGE = "Банкомат не найден с id ";

    @Mock
    private AtmRepository repository;
    @Mock
    private AtmMapper mapper;
    @Mock
    private EntityNotFoundSupplier supplier;
    @InjectMocks
    private AtmServiceImpl service;

    @Test
    @DisplayName("поиск по списку ids, позитивный сценарий")
    void findAllByIdsPositiveTest() {
        var ids = Arrays.asList(1L, 2L, 3L);
        var mockEntities = Arrays.asList(new AtmEntity(), new AtmEntity(), new AtmEntity());
        var expectedDtos = Arrays.asList(new AtmDto(), new AtmDto(), new AtmDto());

        when(repository.findAllById(ids)).thenReturn(mockEntities);
        when(mapper.toDtoList(mockEntities)).thenReturn(expectedDtos);

        List<AtmDto> result = service.findAllById(ids);

        assertThat(result).isEqualTo(expectedDtos);
    }

    @Test
    @DisplayName("поиск по списку ids, негативный сценарий")
    void findAllByIdsShouldThrowExceptionNegativeTest() {
        var ids = Arrays.asList(1L, 2L, 3L);
        var foundAtms = Arrays.asList(new AtmEntity(), new AtmEntity());

        when(repository.findAllById(ids)).thenReturn(foundAtms);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        service.findAllById(ids);

        verify(supplier).checkForSizeAndLogging(messageCaptor.capture(), any(), any());

        assertEquals(MESSAGE, messageCaptor.getValue());
    }

    @Test
    @DisplayName("создание записи, позитивный сценарий")
    void createPositiveTest() {
        var inputDto = new AtmDto();
        var savedEntity = new AtmEntity();

        when(mapper.toEntity(inputDto)).thenReturn(savedEntity);
        when(repository.save(savedEntity)).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(inputDto);

        service.create(inputDto);

        verify(mapper).toEntity(inputDto);
        verify(repository).save(savedEntity);
        verify(mapper).toDto(savedEntity);
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    @DisplayName("создание записи, негативный сценарий")
    void createShouldThrowExceptionNegativeTest() {
        var inputDto = new AtmDto();
        var savedEntity = new AtmEntity();

        when(mapper.toEntity(inputDto)).thenReturn(savedEntity);
        when(repository.save(savedEntity)).thenThrow(new RuntimeException("Тест сообщения при неудачном сохранении"));

        assertThrows(RuntimeException.class, () -> service.create(inputDto));

        verify(mapper).toEntity(inputDto);
        verify(repository).save(savedEntity);
        verifyNoMoreInteractions(mapper, repository);
    }


    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updatePositiveTest() {
        var atmId = 1L;
        var inputDto = new AtmDto();

        var existingEntity = new AtmEntity();
        when(repository.findById(atmId)).thenReturn(Optional.of(existingEntity));

        var updatedEntity = new AtmEntity();
        when(mapper.mergeToEntity(inputDto, existingEntity)).thenReturn(updatedEntity);

        var expectedDto = new AtmDto();
        when(mapper.toDto(updatedEntity)).thenReturn(expectedDto);

        var result = service.update(atmId, inputDto);

        verify(repository).findById(atmId);
        verify(mapper).mergeToEntity(inputDto, existingEntity);
        verify(mapper).toDto(updatedEntity);
        verifyNoMoreInteractions(repository, mapper);

        assertEquals(expectedDto, result);
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateNonExistingIdNegativeTest() {
        var id = 999L;
        var atmDto = new AtmDto();

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(supplier
                .getException(MESSAGE, id))
                .thenReturn(new EntityNotFoundException("test message"));

        var exception = assertThrows(EntityNotFoundException.class,
                () -> service.update(id, atmDto));

        verifyNoMoreInteractions(repository, mapper, supplier);

        assertEquals("test message", exception.getMessage());
    }

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        var id = 1L;
        var atmEntity = new AtmEntity();
        var expectedDto = new AtmDto();

        when(repository.findById(id)).thenReturn(Optional.of(atmEntity));
        when(mapper.toDto(atmEntity)).thenReturn(expectedDto);

        AtmDto resultDto = service.findById(id);

        verify(repository).findById(id);
        verify(mapper).toDto(atmEntity);
        verifyNoMoreInteractions(repository, mapper, supplier);

        assertEquals(expectedDto, resultDto);
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByIdNonExistingIdNegativeTest() {
        Long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(supplier.getException(MESSAGE, id))
                .thenReturn(new EntityNotFoundException("test message"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.findById(id));

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository, mapper, supplier);

        assertEquals("test message", exception.getMessage());
    }
}
