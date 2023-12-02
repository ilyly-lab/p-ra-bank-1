package com.bank.transfer.mapper;

import com.bank.transfer.dto.PhoneTransferDto;
import com.bank.transfer.entity.PhoneTransferEntity;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;


class PhoneTransferMapperImplTest {
    private final PhoneTransferMapper phoneTransferMapper = new PhoneTransferMapperImpl();

    @Test
    @DisplayName("Маппинг в Entity и проверка на null")
    void toEntityPositiveAndNullTest() {
        PhoneTransferDto transferDto = getDto1();

        PhoneTransferEntity transferEntity = phoneTransferMapper.toEntity(transferDto);
        PhoneTransferEntity nullTransferEntity = phoneTransferMapper.toEntity(null);

        assertAll(
                () -> assertNull(nullTransferEntity),
                () -> assertEquals(transferDto.getPhoneNumber(), transferEntity.getPhoneNumber()),
                () -> assertEquals(transferDto.getAmount(), transferEntity.getAmount()),
                () -> assertEquals(transferDto.getPurpose(), transferEntity.getPurpose()),
                () -> assertEquals(transferDto.getAccountDetailsId(), transferEntity.getAccountDetailsId())
        );
    }

    @Test
    @DisplayName("Маппинг в Dto и проверка на null")
    void toDtoPositiveAndNullTest() {
        PhoneTransferEntity transferEntity = getEntity1();

        PhoneTransferDto transferDto = phoneTransferMapper.toDto(transferEntity);
        PhoneTransferDto nullTransferDto = phoneTransferMapper.toDto(null);

        assertAll(
                () -> assertNull(nullTransferDto),
                () -> assertEquals(transferEntity.getPhoneNumber(), transferDto.getPhoneNumber()),
                () -> assertEquals(transferEntity.getAmount(), transferDto.getAmount()),
                () -> assertEquals(transferEntity.getPurpose(), transferDto.getPurpose()),
                () -> assertEquals(transferEntity.getAccountDetailsId(), transferDto.getAccountDetailsId())
        );
    }

    @Test
    @DisplayName("Маппинг новых данных из дто в энтити и проверка на null")
    void mergeToEntityPositiveAndNullTest() {
        PhoneTransferEntity transferEntity = getEntity1();
        PhoneTransferDto transferDto = getDto1();

        PhoneTransferEntity mergeEntity = phoneTransferMapper.mergeToEntity(transferDto, transferEntity);
        PhoneTransferEntity nullMergeEntity = phoneTransferMapper.mergeToEntity(null, transferEntity);

        assertAll(
                () -> assertEquals(nullMergeEntity, transferEntity),
                () -> assertEquals(mergeEntity.getPhoneNumber(), transferDto.getPhoneNumber()),
                () -> assertEquals(mergeEntity.getAmount(), transferDto.getAmount()),
                () -> assertEquals(mergeEntity.getPurpose(), transferDto.getPurpose()),
                () -> assertEquals(mergeEntity.getAccountDetailsId(), transferDto.getAccountDetailsId())
        );
    }

    @Test
    @DisplayName("Маппинг в List<дто> и проверка на null")
    void toDtoListPositiveAndNullTest() {
        List<PhoneTransferEntity> phoneTransferEntityList =
                new ArrayList<>(List.of(Arrays.array(getEntity1(), getEntity2())));
        List<PhoneTransferDto> expectedPhoneTransferDtoList =
                new ArrayList<>(List.of(Arrays.array(getDto1(), getDto2())));

        List<PhoneTransferDto> actualPhoneTransferDtoList = phoneTransferMapper.toDtoList(phoneTransferEntityList);
        List<PhoneTransferDto> nullActualPhoneTransferDtoList = phoneTransferMapper.toDtoList(null);

        assertAll(
                () -> assertNull(nullActualPhoneTransferDtoList),
                () -> assertEquals(actualPhoneTransferDtoList, expectedPhoneTransferDtoList)
        );
    }

    private PhoneTransferEntity getEntity1() {
        PhoneTransferEntity transferEntity = new PhoneTransferEntity();
        transferEntity.setPhoneNumber(123456L);
        transferEntity.setAmount(BigDecimal.valueOf(100));
        transferEntity.setPurpose("Test purpose");
        transferEntity.setAccountDetailsId(1L);
        return transferEntity;
    }

    private PhoneTransferEntity getEntity2() {
        PhoneTransferEntity transferEntity = new PhoneTransferEntity();
        transferEntity.setPhoneNumber(2L);
        transferEntity.setAmount(BigDecimal.valueOf(7777L));
        transferEntity.setPurpose("Test purpose");
        transferEntity.setAccountDetailsId(2L);
        return transferEntity;
    }

    private PhoneTransferDto getDto1() {
        PhoneTransferDto transferDto = new PhoneTransferDto();
        transferDto.setPhoneNumber(123456L);
        transferDto.setAmount(BigDecimal.valueOf(100));
        transferDto.setPurpose("Test purpose");
        transferDto.setAccountDetailsId(1L);
        return transferDto;
    }


    private PhoneTransferDto getDto2() {
        PhoneTransferDto transferDto = new PhoneTransferDto();
        transferDto.setPhoneNumber(2L);
        transferDto.setAmount(BigDecimal.valueOf(7777L));
        transferDto.setPurpose("Test purpose");
        transferDto.setAccountDetailsId(2L);
        return transferDto;
    }
}