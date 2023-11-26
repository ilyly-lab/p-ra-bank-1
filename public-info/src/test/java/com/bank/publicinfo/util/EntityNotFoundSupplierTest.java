package com.bank.publicinfo.util;

import com.bank.publicinfo.entity.AtmEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EntityNotFoundSupplierTest {
    private final String testMsg = "Test Message";
    private final List<Long> ids = Arrays.asList(1L, 2L, 3L);
    private final List<AtmEntity> entities = Arrays.asList(new AtmEntity(), new AtmEntity());

    @InjectMocks
    private EntityNotFoundSupplier entityNotFoundSupplier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getExceptionTest() {

        var exception = entityNotFoundSupplier.getException(testMsg, ids.get(0));

        assertEquals(ids.get(0) + testMsg, exception.getMessage());
    }

    @Test
    void checkForSizeAndLoggingTest() {

        var exception = assertThrows(EntityNotFoundException.class,
                () -> entityNotFoundSupplier.checkForSizeAndLogging(testMsg, ids, entities));

        assertTrue(exception.getMessage().contains(testMsg));
        assertTrue(exception.getMessage().contains(entities.toString()));
        assertTrue(exception.getMessage().contains(ids.toString()));
    }
}