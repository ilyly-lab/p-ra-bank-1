package com.bank.antifraud.service.impl;

import com.bank.antifraud.dto.AuditDto;
import com.bank.antifraud.entity.AuditEntity;
import com.bank.antifraud.mappers.AuditMapper;
import com.bank.antifraud.repository.AuditRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    AuditRepository repository;
    @Mock
    AuditMapper mapper;
    @InjectMocks
    AuditServiceImpl auditService;

    @Test
    @DisplayName("поиск AuditEntity по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AuditEntity auditEntity = new AuditEntity(10L, "entityType",
                "operationType", "createdBy", "modifiedBy",
                new Timestamp(10L), new Timestamp(20L), "newEntityJson",
                "entityJson");
        AuditDto auditDto = new AuditDto(10L, "entityType", "operationType",
                "createdBy", "modifiedBy", new Timestamp(10L), new Timestamp(20L),
                "newEntityJson", "entityJson");
        doReturn(Optional.of(auditEntity)).when(repository).findById(auditEntity.getId());
        doReturn(auditDto).when(mapper).toDto(auditEntity);

        AuditDto actualResult = auditService.findById(auditEntity.getId());

        assertEquals(actualResult, auditDto);
    }

    @Test
    @DisplayName("поиск AuditEntity по id, негативный сценарий")
    void findByNonExistIdNegativeTest() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> auditService.findById(1L)
        );

        assertEquals(exception.getMessage(), "Не найден аудит с ID  " + 1L);
    }

}