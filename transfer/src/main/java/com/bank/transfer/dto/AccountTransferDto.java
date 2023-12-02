package com.bank.transfer.dto;

import com.bank.transfer.entity.AccountTransferEntity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.experimental.FieldDefaults;



import java.io.Serializable;
import java.math.BigDecimal;

/**
 * ДТО {@link AccountTransferEntity}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountTransferDto implements Serializable {

    Long id;
    Long accountNumber;
    BigDecimal amount;
    String purpose;
    Long accountDetailsId;
}
