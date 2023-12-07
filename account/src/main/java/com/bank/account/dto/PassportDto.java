package com.bank.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO объект для передачи информации о паспорте.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassportDto implements Serializable {
    Long id;
    Integer series;
    Long number;
    String lastName;
    String firstName;
    String middleName;
    String gender;
    LocalDate birthDate;
    String birthPlace;
    String issuedBy;
    LocalDate dateOfIssue;
    Integer divisionCode;
    LocalDate expirationDate;
    RegistrationDto registration;
}
