package com.bank.transfer.mapper;

import com.bank.transfer.dto.CardTransferDto;
import com.bank.transfer.entity.CardTransferEntity;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

class CardTransferMapperImplTest {
    private final CardTransferMapper cardTransferMapper = new CardTransferMapperImpl();

    @Test
    @DisplayName("Маппинг в Entity и проверка на null")
    void toEntityPositiveAndNullTest() {
        CardTransferDto transferDto = getDto1();

        CardTransferEntity transferEntity = cardTransferMapper.toEntity(transferDto);
        CardTransferEntity nullTransferEntity = cardTransferMapper.toEntity(null);

        assertAll(
                () -> assertNull(nullTransferEntity),
                () -> assertEquals(transferDto.getCardNumber(), transferEntity.getCardNumber()),
                () -> assertEquals(transferDto.getAmount(), transferEntity.getAmount()),
                () -> assertEquals(transferDto.getPurpose(), transferEntity.getPurpose()),
                () -> assertEquals(transferDto.getAccountDetailsId(), transferEntity.getAccountDetailsId())
        );
    }

    @Test
    @DisplayName("Маппинг в Dto и проверка на null")
    void toDtoPositiveAndNullTest() {
        CardTransferEntity transferEntity = getEntity1();

        CardTransferDto transferDto = cardTransferMapper.toDto(transferEntity);
        CardTransferDto nullTransferDto = cardTransferMapper.toDto(null);

        assertAll(
                () -> assertNull(nullTransferDto),
                () -> assertEquals(transferEntity.getCardNumber(), transferDto.getCardNumber()),
                () -> assertEquals(transferEntity.getAmount(), transferDto.getAmount()),
                () -> assertEquals(transferEntity.getPurpose(), transferDto.getPurpose()),
                () -> assertEquals(transferEntity.getAccountDetailsId(), transferDto.getAccountDetailsId())
        );
    }

    @Test
    @DisplayName("Маппинг новых данных из дто в энтити и проверка на null")
    void mergeToEntityPositiveAndNullTest() {
        CardTransferEntity transferEntity = getEntity1();

        CardTransferDto transferDto = getDto1();

        CardTransferEntity mergeEntity = cardTransferMapper.mergeToEntity(transferDto, transferEntity);
        CardTransferEntity nullMergeEntity = cardTransferMapper.mergeToEntity(null, transferEntity);

        assertAll(
                () -> assertEquals(nullMergeEntity, transferEntity),
                () -> assertEquals(mergeEntity.getCardNumber(), transferDto.getCardNumber()),
                () -> assertEquals(mergeEntity.getAmount(), transferDto.getAmount()),
                () -> assertEquals(mergeEntity.getPurpose(), transferDto.getPurpose()),
                () -> assertEquals(mergeEntity.getAccountDetailsId(), transferDto.getAccountDetailsId())
        );
    }

    @Test
    @DisplayName("Маппинг в List<дто> и проверка на null")
    void toDtoListPositiveAndNullTest() {
        List<CardTransferEntity> cardTransferEntityList =
                new ArrayList<>(List.of(Arrays.array(getEntity1(), getEntity2())));
        List<CardTransferDto> expectedCardTransferDtoList =
                new ArrayList<>(List.of(Arrays.array(getDto1(), getDto2())));

        List<CardTransferDto> actualCardTransferDtoList = cardTransferMapper.toDtoList(cardTransferEntityList);
        List<CardTransferDto> nullActualCardTransferDtoList = cardTransferMapper.toDtoList(null);

        assertAll(
                () -> assertNull(nullActualCardTransferDtoList),
                () -> assertEquals(actualCardTransferDtoList, expectedCardTransferDtoList)
        );
    }

    private CardTransferEntity getEntity1() {
        CardTransferEntity transferEntity = new CardTransferEntity();
        transferEntity.setCardNumber(123456L);
        transferEntity.setAmount(BigDecimal.valueOf(100));
        transferEntity.setPurpose("Test purpose");
        transferEntity.setAccountDetailsId(1L);
        return transferEntity;
    }

    private CardTransferEntity getEntity2() {
        CardTransferEntity transferEntity = new CardTransferEntity();
        transferEntity.setCardNumber(2L);
        transferEntity.setAmount(BigDecimal.valueOf(7777L));
        transferEntity.setPurpose("Test purpose");
        transferEntity.setAccountDetailsId(2L);
        return transferEntity;
    }

    private CardTransferDto getDto1() {
        CardTransferDto transferDto = new CardTransferDto();
        transferDto.setCardNumber(123456L);
        transferDto.setAmount(BigDecimal.valueOf(100));
        transferDto.setPurpose("Test purpose");
        transferDto.setAccountDetailsId(1L);
        return transferDto;
    }


    private CardTransferDto getDto2() {
        CardTransferDto transferDto = new CardTransferDto();
        transferDto.setCardNumber(2L);
        transferDto.setAmount(BigDecimal.valueOf(7777L));
        transferDto.setPurpose("Test purpose");
        transferDto.setAccountDetailsId(2L);
        return transferDto;
    }
}