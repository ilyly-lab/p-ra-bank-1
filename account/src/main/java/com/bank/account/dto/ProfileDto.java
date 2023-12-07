package com.bank.account.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

/**
 * DTO объект для передачи информации о профиле.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileDto implements Serializable {
    Long id;
    Long phoneNumber;
    String email;
    String nameOnCard;
    Long inn;
    Long snils;
    PassportDto passport;
    ActualRegistrationDto actualRegistration;
}
