package com.bank.account.service;

import com.bank.account.dto.AuditDto;
import com.bank.account.entity.AuditEntity;
import com.bank.account.mapper.AccountAuditMapper;
import com.bank.account.repository.AccountAuditRepository;
import com.bank.account.service.common.ExceptionReturner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class AccountAuditServiceImplTest {

    @Mock
    AccountAuditRepository repository;
    @Mock
    AccountAuditMapper mapper;
    @Mock
    ExceptionReturner exceptionReturner;
    @InjectMocks
    AccountAuditServiceImpl accountAuditService;

    @Test
    @DisplayName("поиск по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AuditEntity auditEntity = getAuditEntity();
        AuditDto auditDto = getAuditDto();
        doReturn(Optional.of(auditEntity)).when(repository).findById(auditEntity.getId());
        doReturn(auditDto).when(mapper).toDto(auditEntity);

        AuditDto actualResult = accountAuditService.findById(auditEntity.getId());

        assertEquals(actualResult, auditDto);
    }

    @Test
    @DisplayName("поиск по id, негативный сценарий")
    void findByNonExistIdNegativeNest() {
        Long invalidId = 1L;

        when(repository.findById(invalidId)).thenReturn(Optional.empty());
        when(exceptionReturner.getEntityNotFoundException("Не существующий id = " + invalidId))
                .thenReturn(new EntityNotFoundException("Не существующий id = " + invalidId));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accountAuditService.findById(invalidId)
        );
        assertEquals("Не существующий id = " + invalidId, exception.getMessage());

        verify(mapper, never()).toDto(any());
    }

    private AuditDto getAuditDto() {
        return new AuditDto(10L, "entityType", "operationType",
                "createdBy", "modifiedBy", new Timestamp(10L), new Timestamp(20L),
                "newEntityJson", "entityJson");
    }

    private AuditEntity getAuditEntity() {
        return new AuditEntity(10L, "entityType", "operationType",
                "createdBy", "modifiedBy", new Timestamp(10L), new Timestamp(20L),
                "newEntityJson", "entityJson");
    }
}