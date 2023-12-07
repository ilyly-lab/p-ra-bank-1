package com.bank.authorization.service;

import com.bank.authorization.dto.AuditDto;
import com.bank.authorization.entity.AuditEntity;
import com.bank.authorization.mapper.AuditMapper;
import com.bank.authorization.repository.AuditRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AuditServiceImplTest {

    @Mock
    private AuditRepository repository;
    @Mock
    private AuditMapper mapper;
    @InjectMocks
    private AuditServiceImpl auditService;

    @Test
    @DisplayName("поиск аудита по id, позитивный сценарий")
    void findByIdPositiveTest() {
        AuditEntity auditEntity = getAuditEntity();
        AuditDto auditDto = getAuditDto();
        doReturn(Optional.of(auditEntity)).when(repository).findById(auditEntity.getId());
        doReturn(auditDto).when(mapper).toDto(auditEntity);

        Optional<AuditDto> actualResult = Optional.ofNullable(auditService.findById(auditEntity.getId()));

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(auditDto);
    }

    @Test
    @DisplayName("поиск несуществующего аудита по id, негативный сценарий")
    void findByIdNegativeTest() {
        doReturn(Optional.empty()).when(repository).findById(any());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> auditService.findById(null));
        assertThat("Не найден аудит с ID  " + null).isEqualTo(exception.getMessage());
        verifyNoInteractions(mapper);
    }

    private AuditDto getAuditDto() {
        return new AuditDto(10l,
                "entityType",
                "operationType",
                "createdBy",
                "modifiedBy",
                new Timestamp(10L),
                new Timestamp(20L),
                "newEntityJson",
                "entityJson");
    }

    private AuditEntity getAuditEntity() {
        return new AuditEntity(10l,
                "entityType",
                "operationType",
                "createdBy",
                "modifiedBy",
                new Timestamp(10L),
                new Timestamp(20L),
                "newEntityJson",
                "entityJson");
    }
}