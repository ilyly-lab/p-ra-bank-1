package com.bank.transfer.service.Impl;

import com.bank.transfer.dto.AuditDto;

import com.bank.transfer.entity.AuditEntity;
import com.bank.transfer.mapper.AuditMapper;
import com.bank.transfer.repository.AuditRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {
    @Mock
    AuditRepository repository;
    @Mock
    AuditMapper mapper;
    @InjectMocks
    AuditServiceImpl auditService;

    @Test
    @DisplayName("Получение Audit по id и проверка на null")
    void findByIdPositiveTestAndNullTest() {
        Long accountId = 1L;
        Long notFoundEntityWithId = 2L;
        AuditEntity entity = getEntity();
        AuditDto dto = getDto();

        doReturn(Optional.of(entity)).when(repository).findById(accountId);
        doReturn(dto).when(mapper).toDto(entity);
        doThrow(EntityNotFoundException.class).when(repository).findById(notFoundEntityWithId);

        AuditDto result = auditService.findById(accountId);
        assertAll(
                () -> assertThat(result).isEqualTo(dto),
                () -> assertThrows(EntityNotFoundException.class,
                        () -> auditService.findById(notFoundEntityWithId)),
                () -> verify(mapper).toDto(entity),
                () -> verify(repository).findById(accountId)
        );
    }

    private AuditEntity getEntity() {
        return new AuditEntity(1L,
                "typeEntity1", "operationType1", "createdType1",
                "modifiedBy", new Timestamp(23), new Timestamp(11),
                "newEntityJson", "EntityJson");
    }

    private AuditDto getDto() {
        return new AuditDto(1L,
                "typeEntity1", "operationType1", "createdType1",
                "modifiedBy", new Timestamp(23), new Timestamp(11),
                "newEntityJson", "EntityJson");
    }
}