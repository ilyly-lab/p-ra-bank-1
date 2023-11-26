package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.LicenseDto;
import com.bank.publicinfo.entity.LicenseEntity;
import com.bank.publicinfo.mapper.LicenseMapper;
import com.bank.publicinfo.repository.LicenseRepository;
import com.bank.publicinfo.util.EntityNotFoundSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class LicenseServiceImplTest {

    private final static String MESSAGE = "Лицензии не найдено с id ";

    @Mock
    private LicenseRepository repository;
    @Mock
    private LicenseMapper mapper;
    @Mock
    private EntityNotFoundSupplier supplier;
    @InjectMocks
    private LicenseServiceImpl service;

    @Test
    @DisplayName("поиск по списку ids, позитивный сценарий")
    void findAllByIdPositiveTest() {
        var ids = Arrays.asList(1L, 2L, 3L);
        var mockEntities = Arrays.asList(new LicenseEntity(), new LicenseEntity(), new LicenseEntity());
        var expectedDtos = Arrays.asList(new LicenseDto(), new LicenseDto(), new LicenseDto());

        when(repository.findAllById(ids)).thenReturn(mockEntities);
        when(mapper.toDtoList(mockEntities)).thenReturn(expectedDtos);

        var result = service.findAllById(ids);

        assertThat(result).isEqualTo(expectedDtos);
    }

    @Test
    @DisplayName("поиск по списку ids, негативный сценарий")
    void findAllByIdShouldThrowExceptionNegativeTest() {
        var ids = Arrays.asList(1L, 2L, 3L);
        var foundEntities = Arrays.asList(new LicenseEntity(), new LicenseEntity());

        when(repository.findAllById(ids)).thenReturn(foundEntities);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        service.findAllById(ids);

        verify(supplier).checkForSizeAndLogging(messageCaptor.capture(), any(), any());

        assertEquals(MESSAGE, messageCaptor.getValue());
    }

    @Test
    @DisplayName("создание записи, позитивный сценарий")
    void createPositiveTest() {
        var inputDto = new LicenseDto();
        var savedEntity = new LicenseEntity();

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
    @DisplayName("поиск по списку ids, негативный сценарий")
    void createNegativeTest() {
        var inputDto = new LicenseDto();
        var savedEntity = new LicenseEntity();

        when(mapper.toEntity(inputDto)).thenReturn(savedEntity);
        when(repository.save(savedEntity)).thenThrow(new RuntimeException("Тест сообщения при неудачном сохранении"));

        assertThrows(RuntimeException.class, () -> service.create(inputDto));

        verify(mapper).toEntity(inputDto);
        verify(repository).save(savedEntity);
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    @DisplayName("обновление по id, позитивный сценарий")
    void updatePositiveTestTest() {
        Long id = 1L;
        var inputDto = new LicenseDto();

        var existingEntity = new LicenseEntity();
        when(repository.findById(id)).thenReturn(Optional.of(existingEntity));

        var updatedEntity = new LicenseEntity();
        when(mapper.mergeToEntity(inputDto, existingEntity)).thenReturn(updatedEntity);

        var expectedDto = new LicenseDto();
        when(mapper.toDto(updatedEntity)).thenReturn(expectedDto);

        var result = service.update(id, inputDto);

        verify(repository).findById(id);
        verify(mapper).mergeToEntity(inputDto, existingEntity);
        verify(mapper).toDto(updatedEntity);
        verifyNoMoreInteractions(repository, mapper);

        assertEquals(expectedDto, result);
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateNonExistingIdNegativeTest() {
        Long id = 999L;
        var licenseDto = new LicenseDto();

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(supplier
                .getException(MESSAGE, id))
                .thenReturn(new EntityNotFoundException("test message"));

        var exception = assertThrows(EntityNotFoundException.class,
                () -> service.update(id, licenseDto));

        verifyNoMoreInteractions(repository, mapper, supplier);

        assertEquals("test message", exception.getMessage());
    }

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        Long id = 1L;
        var licenseEntity = new LicenseEntity();
        var expectedDto = new LicenseDto();

        when(repository.findById(id)).thenReturn(Optional.of(licenseEntity));
        when(mapper.toDto(licenseEntity)).thenReturn(expectedDto);

        var resultDto = service.findById(id);

        verify(repository).findById(id);
        verify(mapper).toDto(licenseEntity);
        verifyNoMoreInteractions(repository, mapper, supplier);

        assertEquals(expectedDto, resultDto);
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByIdNonExistingIdNegativeTest() {
        Long id = 9999L;

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(supplier.getException(MESSAGE, id))
                .thenReturn(new EntityNotFoundException("test message"));

        var exception = assertThrows(EntityNotFoundException.class,
                () -> service.findById(id));

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository, mapper, supplier);

        assertEquals("test message", exception.getMessage());
    }
}