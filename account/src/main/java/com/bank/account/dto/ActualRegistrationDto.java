package com.bank.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * DTO объект для передачи информации об актуальной регистрации.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActualRegistrationDto implements Serializable {
    Long id;
    String country;
    String region;
    String city;
    String district;
    String locality;
    String street;
    String houseNumber;
    String houseBlock;
    String flatNumber;
    Long index;
}
