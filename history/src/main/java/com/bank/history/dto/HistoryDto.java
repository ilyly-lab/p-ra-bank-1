package com.bank.history.dto;

import com.bank.history.entity.HistoryEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;


/**
 * Dto для {@link HistoryEntity}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HistoryDto {
    Long id;
    Long transferAuditId;
    Long profileAuditId;
    Long accountAuditId;
    Long antiFraudAuditId;
    Long publicBankInfoAuditId;
    Long authorizationAuditId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HistoryDto that = (HistoryDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(transferAuditId, that.transferAuditId) &&
                Objects.equals(profileAuditId, that.profileAuditId) &&
                Objects.equals(accountAuditId, that.accountAuditId) &&
                Objects.equals(antiFraudAuditId, that.antiFraudAuditId) &&
                Objects.equals(publicBankInfoAuditId, that.publicBankInfoAuditId) &&
                Objects.equals(authorizationAuditId, that.authorizationAuditId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, transferAuditId, profileAuditId,
                accountAuditId, antiFraudAuditId, publicBankInfoAuditId, authorizationAuditId);
    }


}
