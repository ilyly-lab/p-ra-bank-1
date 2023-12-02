package com.bank.transfer.service.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class EntityNotFoundReturnerTest {
    @InjectMocks
    EntityNotFoundReturner entityNotFoundReturner = new EntityNotFoundReturner();

    @Test
    @DisplayName("Выбрасывание EntityNotFoundException")
    void getEntityNotFoundExceptionTest() {
        Long id = 1L;
        String message = "Не найдена сущность с ID ";
        String expectedMessage = message + id;

        EntityNotFoundException exception = entityNotFoundReturner.getEntityNotFoundException(id, message);

        assertEquals(expectedMessage, exception.getMessage());
    }
}
