package com.bank.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO объект для передачи информации о деталях банка.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankDetailsDto implements Serializable {
    Long id;
    Long bik;
    Long inn;
    Long kpp;
    BigDecimal corAccount;
    String city;
    String jointStockCompany;
    String name;
}
