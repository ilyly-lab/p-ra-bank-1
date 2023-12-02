package com.bank.transfer.mapper;

import com.bank.transfer.dto.AccountTransferDto;
import com.bank.transfer.entity.AccountTransferEntity;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertAll;

class AccountTransferMapperImplTest {
    private final AccountTransferMapper accountTransferMapper = new AccountTransferMapperImpl();

    @Test
    @DisplayName("Maппинг в Entity и проверка на null")
    void toEntityPositiveAndNullTest() {
        AccountTransferDto transferDto = getDto1();

        AccountTransferEntity transferEntity = accountTransferMapper.toEntity(transferDto);
        AccountTransferEntity nullTransferEntity = accountTransferMapper.toEntity(null);

        assertAll("Маппинг в энтити и проверка на null",
                () -> assertNull(nullTransferEntity),
                () -> assertEquals(transferDto.getAccountNumber(), transferEntity.getAccountNumber()),
                () -> assertEquals(transferDto.getAmount(), transferEntity.getAmount()),
                () -> assertEquals(transferDto.getPurpose(), transferEntity.getPurpose()),
                () -> assertEquals(transferDto.getAccountDetailsId(), transferEntity.getAccountDetailsId())
        );
    }

    @Test
    @DisplayName("Маппинг в Dto и проверка на null")
    void toDtoPositiveAndNullTest() {
        AccountTransferEntity transferEntity = getEntity1();

        AccountTransferDto transferDto = accountTransferMapper.toDto(transferEntity);
        AccountTransferDto nullTransferDto = accountTransferMapper.toDto(null);

        assertAll("Маппинг в дто и проверка на null",
                () -> assertNull(nullTransferDto),
                () -> assertEquals(transferEntity.getAccountNumber(), transferDto.getAccountNumber()),
                () -> assertEquals(transferEntity.getAmount(), transferDto.getAmount()),
                () -> assertEquals(transferEntity.getPurpose(), transferDto.getPurpose()),
                () -> assertEquals(transferEntity.getAccountDetailsId(), transferDto.getAccountDetailsId())
        );
    }

    @Test
    @DisplayName("Маппинг новых данных из дто в энтити и проверка на null")
    void mergeToEntityPositiveAndNullTest() {
        AccountTransferEntity transferEntity = getEntity1();

        AccountTransferDto transferDto = new AccountTransferDto();
        transferDto.setAccountNumber(123456999L);
        transferDto.setAmount(BigDecimal.valueOf(100999));
        transferDto.setPurpose("Test purpose999");
        transferDto.setAccountDetailsId(1999L);

        AccountTransferEntity mergeEntity = accountTransferMapper.mergeToEntity(transferDto, transferEntity);
        AccountTransferEntity nullMergeEntity = accountTransferMapper.mergeToEntity(null, transferEntity);

        assertAll("Маппинг новых данных из дто в энтити и проверка на null",
                () -> assertEquals(nullMergeEntity, transferEntity),
                () -> assertEquals(mergeEntity.getAccountNumber(), transferDto.getAccountNumber()),
                () -> assertEquals(mergeEntity.getAmount(), transferDto.getAmount()),
                () -> assertEquals(mergeEntity.getPurpose(), transferDto.getPurpose()),
                () -> assertEquals(mergeEntity.getAccountDetailsId(), transferDto.getAccountDetailsId())
        );
    }

    @Test
    @DisplayName("Маппинг в лист<дто> и проверка на null")
    void toDtoListPositiveAndNullTest() {
        List<AccountTransferEntity> accountTransferEntityList =
                new ArrayList<>(List.of(Arrays.array(getEntity1(), getEntity2())));
        List<AccountTransferDto> expectedAccountTransferDtoList =
                new ArrayList<>(List.of(Arrays.array(getDto1(), getDto2())));

        List<AccountTransferDto> actualAccountTransferDtoList =
                accountTransferMapper.toDtoList(accountTransferEntityList);
        List<AccountTransferDto> nullActualAccountTransferDtoList =
                accountTransferMapper.toDtoList(null);

        assertAll(
                () -> assertNull(nullActualAccountTransferDtoList),
                () -> assertEquals(actualAccountTransferDtoList, expectedAccountTransferDtoList)
        );
    }

    private AccountTransferEntity getEntity1() {
        AccountTransferEntity transferEntity = new AccountTransferEntity();
        transferEntity.setAccountNumber(123456L);
        transferEntity.setAmount(BigDecimal.valueOf(100));
        transferEntity.setPurpose("Test purpose");
        transferEntity.setAccountDetailsId(1L);
        return transferEntity;
    }

    private AccountTransferEntity getEntity2() {
        AccountTransferEntity transferEntity = new AccountTransferEntity();
        transferEntity.setAccountNumber(2L);
        transferEntity.setAmount(BigDecimal.valueOf(7777L));
        transferEntity.setPurpose("Test purpose");
        transferEntity.setAccountDetailsId(2L);
        return transferEntity;
    }

    private AccountTransferDto getDto1() {
        AccountTransferDto transferDto = new AccountTransferDto();
        transferDto.setAccountNumber(123456L);
        transferDto.setAmount(BigDecimal.valueOf(100));
        transferDto.setPurpose("Test purpose");
        transferDto.setAccountDetailsId(1L);
        return transferDto;
    }


    private AccountTransferDto getDto2() {
        return AccountTransferDto.builder()
                .accountNumber(2L)
                .amount(BigDecimal.valueOf(7777L))
                .purpose("Test purpose")
                .accountDetailsId(2L)
                .build();
    }
}