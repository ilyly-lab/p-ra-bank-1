package com.bank.antifraud.service.common;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionReturnerTest {

    @Test
    void getEntityNotFoundException() {
        String message = "Entity not found";

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            throw new ExceptionReturner().getEntityNotFoundException(message);
        });

        assertEquals(message, exception.getMessage());
    }
}