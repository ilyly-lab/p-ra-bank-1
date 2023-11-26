package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.BranchDto;
import com.bank.publicinfo.entity.BranchEntity;
import com.bank.publicinfo.mapper.BranchMapper;
import com.bank.publicinfo.repository.BranchRepository;
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
class BranchServiceImplTest {

    @Mock
    private BranchRepository repository;
    @Mock
    private BranchMapper mapper;
    @Mock
    private EntityNotFoundSupplier supplier;
    @InjectMocks
    private BranchServiceImpl service;

    @Test
    @DisplayName("поиск по списку ids, позитивный сценарий")
    void findAllByIdPositiveTest() {
        var ids = Arrays.asList(1L, 2L, 3L);
        var mockEntities = Arrays.asList(new BranchEntity(), new BranchEntity(), new BranchEntity());
        var expectedDtos = Arrays.asList(new BranchDto(), new BranchDto(), new BranchDto());

        when(repository.findAllById(ids)).thenReturn(mockEntities);
        when(mapper.toDtoList(mockEntities)).thenReturn(expectedDtos);

        var result = service.findAllById(ids);

        assertThat(result).isEqualTo(expectedDtos);
    }

    @Test
    @DisplayName("поиск по списку ids, негативный сценарий")
    void findAllByIdShouldThrowExceptionNegativeTest() {
        var ids = Arrays.asList(1L, 2L, 3L);
        var foundEntities = Arrays.asList(new BranchEntity(), new BranchEntity());

        when(repository.findAllById(ids)).thenReturn(foundEntities);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        service.findAllById(ids);

        verify(supplier).checkForSizeAndLogging(messageCaptor.capture(), any(), any());

        assertEquals("Информации об отделении не найдено с id ", messageCaptor.getValue());
    }

    @Test
    @DisplayName("создание записи, позитивный сценарий")
    void createPositiveTest() {
        var inputDto = new BranchDto();
        var savedEntity = new BranchEntity();

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
    void createNegativeTest() {
        var inputDto = new BranchDto();
        var savedEntity = new BranchEntity();

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
        Long branchId = 1L;
        var inputDto = new BranchDto();

        var existingEntity = new BranchEntity();
        when(repository.findById(branchId)).thenReturn(Optional.of(existingEntity));

        var updatedEntity = new BranchEntity();
        when(mapper.mergeToEntity(inputDto, existingEntity)).thenReturn(updatedEntity);

        var expectedDto = new BranchDto();
        when(mapper.toDto(updatedEntity)).thenReturn(expectedDto);

        var result = service.update(branchId, inputDto);

        verify(repository).findById(branchId);
        verify(mapper).mergeToEntity(inputDto, existingEntity);
        verify(mapper).toDto(updatedEntity);
        verifyNoMoreInteractions(repository, mapper);

        assertEquals(expectedDto, result);
    }

    @Test
    @DisplayName("обновление по несуществующему id, негативный сценарий")
    void updateNonExistingIdNegativeTest() {
        Long id = 999L;
        var branchDto = new BranchDto();

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(supplier
                .getException("Информации об отделении не найдено с id ", id))
                .thenReturn(new EntityNotFoundException("test message"));

        var exception = assertThrows(EntityNotFoundException.class,
                () -> service.update(id, branchDto));

        verifyNoMoreInteractions(repository, mapper, supplier);

        assertEquals("test message", exception.getMessage());
    }

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        Long id = 1L;
        var branchEntity = new BranchEntity();
        var expectedDto = new BranchDto();

        when(repository.findById(id)).thenReturn(Optional.of(branchEntity));
        when(mapper.toDto(branchEntity)).thenReturn(expectedDto);

        var resultDto = service.findById(id);

        verify(repository).findById(id);
        verify(mapper).toDto(branchEntity);
        verifyNoMoreInteractions(repository, mapper, supplier);

        assertEquals(expectedDto, resultDto);
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByIdNonExistingIdNegativeTest() {
        Long id = 9999L;

        when(repository.findById(id)).thenReturn(Optional.empty());
        when(supplier.getException("Информации об отделении не найдено с id ", id))
                .thenReturn(new EntityNotFoundException("test message"));

        var exception = assertThrows(EntityNotFoundException.class,
                () -> service.findById(id));

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository, mapper, supplier);

        assertEquals("test message", exception.getMessage());
    }
}