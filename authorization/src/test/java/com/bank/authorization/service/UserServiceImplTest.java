package com.bank.authorization.service;

import com.bank.authorization.dto.UserDto;
import com.bank.authorization.entity.UserEntity;
import com.bank.authorization.mapper.UserMapper;
import com.bank.authorization.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper mapper;
    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("поиск пользователя по id, позитивный сценарий")
    void findByIdPositiveTest() {
        UserEntity userEntity = getUserEntity();
        UserDto userDto = getUserDto();
        doReturn(Optional.of(userEntity)).when(repository).findById(userEntity.getId());
        doReturn(userDto).when(mapper).toDTO(userEntity);

        Optional<UserDto> actualResult = Optional.ofNullable(userService.findById(userEntity.getId()));

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(userDto);
    }

    @Test
    @DisplayName("поиск несуществующего пользователя по id, негативный сценарий")
    void findByIdNonExistUserNegativeTest() {
        doReturn(Optional.empty()).when(repository).findById(any());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.findById(null));
        assertThat("Не был найден пользователь с ID " + null).isEqualTo(exception.getMessage());
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("сохранение пользователя через дто, позитивный сценарий")
    void saveUserPositiveTest() {
        UserEntity userEntity = getUserEntity();
        UserDto expectedResult = getUserDto();
        doReturn(userEntity).when(mapper).toEntity(expectedResult);
        doReturn(userEntity).when(repository).save(any(UserEntity.class));
        doReturn(expectedResult).when(mapper).toDTO(userEntity);

        Optional<UserDto> actualResult = Optional.ofNullable(userService.save(expectedResult));

        assertThat(actualResult.get().getId()).isEqualTo(expectedResult.getId());
    }

    @Test
    @DisplayName("сохранение пользователя c нулевым аргументом, негативный сценарий")
    void saveUserByNullNegativeTest() {
        UserDto userDto = null;

        assertThrows(NullPointerException.class, () -> userService.save(userDto));
    }

    @Test
    @DisplayName("обновление пользователя по id и через дто, позитивный сценарий")
    void updateUserPistiveTest() {
        UserEntity userEntity = getUserEntity();
        UserDto expectedResult = getUserDto();
        doReturn(Optional.of(userEntity)).when(repository).findById(expectedResult.getId());
        doReturn(userEntity).when(mapper).mergeToEntity(expectedResult, userEntity);
        doReturn(userEntity).when(repository).save(userEntity);
        doReturn(expectedResult).when(mapper).toDTO(userEntity);

        UserDto actualResult = userService.update(expectedResult.getId(), expectedResult);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("обновление пользователя по несуществующему id, негативный сценарий")
    void updateNonExistUserNegativeTest() {
        UserDto userDto = getUserDto();

        assertThrows(EntityNotFoundException.class, () -> userService.update(1L ,userDto));
    }

    @Test
    @DisplayName("Поиск списка пользователей по id, позитивный сценарий")
    void findAllByIdsPositiveTest() {
        List<Long> ids = List.of(1L, 2L, 3L);
        UserEntity entity1 = getUserEntity();
        entity1.setId(1L);
        UserEntity entity2 = getUserEntity();
        entity1.setId(2L);
        UserEntity entity3 = getUserEntity();
        entity1.setId(3L);
        List<UserDto> expectedResult = getUserDtoList();
        doReturn(Optional.of(entity1)).when(repository).findById(1L);
        doReturn(Optional.of(entity2)).when(repository).findById(2L);
        doReturn(Optional.of(entity3)).when(repository).findById(3L);
        doReturn(expectedResult).when(mapper).toDtoList(Arrays.asList(entity1, entity2, entity3));

        List<UserDto> actualResult = userService.findAllByIds(ids);

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    @DisplayName("Поиск списка пользователей по id, где один id не существует, негативный сценарий")
    void findAllByIdsNonExistentUserNegativeTest() {
        List<Long> ids = List.of(1L, 2L, 3L);
        UserEntity entity1 = getUserEntity();
        entity1.setId(1L);
        UserEntity entity2 = getUserEntity();
        entity1.setId(2L);
        doReturn(Optional.of(entity1)).when(repository).findById(1L);
        doReturn(Optional.of(entity2)).when(repository).findById(2L);
        doReturn(Optional.empty()).when(repository).findById(3L);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userService.findAllByIds(ids));

        assertThat("Не был найден пользователь с ID " + 3L).isEqualTo(exception.getMessage());
        verifyNoInteractions(mapper);
    }

    private List<UserDto> getUserDtoList() {
        return new ArrayList<>(Arrays.asList(new UserDto(1L, "USER", "password", 99L),
                new UserDto(2L, "USER", "password", 99L),
                new UserDto(3L, "USER", "password", 99L)));
    }

    private UserDto getUserDto() {
        return new UserDto(20L, "USER", "password", 99L);
    }

    private UserEntity getUserEntity() {
        return new UserEntity(20L, "USER", 99L, "password");
    }
}