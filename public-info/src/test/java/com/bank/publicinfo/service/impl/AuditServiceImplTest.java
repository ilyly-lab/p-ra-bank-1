package com.bank.publicinfo.service.impl;

import com.bank.publicinfo.dto.AuditDto;
import com.bank.publicinfo.entity.AuditEntity;
import com.bank.publicinfo.mapper.AuditMapper;
import com.bank.publicinfo.repository.AuditRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditRepository repository;
    @Mock
    private AuditMapper mapper;
    @InjectMocks
    private AuditServiceImpl service;

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        var id = 1L;
        var auditEntity = new AuditEntity();
        var expectedDto = new AuditDto();

        when(repository.findById(id)).thenReturn(Optional.of(auditEntity));
        when(mapper.toDto(auditEntity)).thenReturn(expectedDto);

        var resultDto = service.findById(id);

        verify(repository).findById(id);
        verify(mapper).toDto(auditEntity);
        verifyNoMoreInteractions(repository, mapper);

        assertEquals(expectedDto, resultDto);
    }

    @Test
    @DisplayName("поиск по несуществующему id, негативный сценарий")
    void findByIdNegativeTest() {
        Long id = 999L;

        doThrow(new EntityNotFoundException("Не найден аудит с ID  " + id)).when(repository).findById(id);

        var exception = assertThrows(EntityNotFoundException.class,
                () -> service.findById(id));

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository, mapper);

        assertEquals("Не найден аудит с ID  " + id, exception.getMessage());
    }
}