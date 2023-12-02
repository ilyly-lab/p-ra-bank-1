package com.bank.transfer.mapper;

import com.bank.transfer.dto.AuditDto;
import com.bank.transfer.entity.AuditEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper для {@link AuditEntity} и {@link AuditDto}
 */
@Mapper(componentModel = "spring")
public interface AuditMapper {

    /**
     * @param audit {@link AuditEntity}
     * @return {@link AuditDto}
     */
    @Mapping(target = "id", ignore = true)
    AuditDto toDto(AuditEntity audit);

    @Mapping(target = "id", ignore = true)
    AuditEntity toEntity(AuditDto audit);
}
