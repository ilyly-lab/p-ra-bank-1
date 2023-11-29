package com.bank.antifraud.dto;

import com.bank.antifraud.entity.AuditEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * Dto для {@link AuditEntity}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuditDto implements Serializable {

    Long id;
    String entityType;
    String operationType;
    String createdBy;
    String modifiedBy;
    Timestamp createdAt;
    Timestamp modifiedAt;
    String newEntityJson;
    String entityJson;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AuditDto audit = (AuditDto) o;
        return Objects.equals(id, audit.id) &&
                Objects.equals(createdBy, audit.createdBy) &&
                Objects.equals(createdAt, audit.createdAt) &&
                Objects.equals(entityJson, audit.entityJson) &&
                Objects.equals(modifiedBy, audit.modifiedBy) &&
                Objects.equals(entityType, audit.entityType) &&
                Objects.equals(modifiedAt, audit.modifiedAt) &&
                Objects.equals(newEntityJson, audit.newEntityJson) &&
                Objects.equals(operationType, audit.operationType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityType, operationType, createdBy, modifiedBy,
                createdAt, modifiedAt, newEntityJson, entityJson);
    }
}
