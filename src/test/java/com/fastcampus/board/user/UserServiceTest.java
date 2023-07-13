package com.fastcampus.board.user;

import com.fastcampus.board.__core.errors.ErrorMessage;
import com.fastcampus.board.__core.errors.exception.DuplicateNickNameException;
import com.fastcampus.board.__core.errors.exception.DuplicateUsernameException;
import com.fastcampus.board.__core.errors.exception.Exception500;
import com.fastcampus.board.user.dto.UserRequest;
import com.fastcampus.board.user.dto.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @DisplayName("회원 가입 성공 테스트")
    @Test
    void save_Success_Test() {
        // Given
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        Mockito.when(passwordEncoder.encode(ArgumentMatchers.any(String.class)))
                .thenAnswer(invocation -> {
                    String rawPassword = invocation.getArgument(0);
                    return "encoded_" + rawPassword;
                });

        UserRequest.JoinDTO joinDTO = UserRequest.JoinDTO.builder()
                .username("testUser")
                .password("1234")
                .email("test@test.com")
                .nickName("testUser")
                .build();

        // When
        UserResponse.JoinDTO actual = userService.save(joinDTO);

        // Then
        Assertions.assertEquals("testUser", actual.getUsername());
        Assertions.assertEquals("testUser", actual.getNickName());
    }

    @DisplayName("회원 수정 성공 테스트")
    @Test
    void update_Success_Test() {
        // Given
        User user = User.builder().username("testUser").nickName("testUser").build();

        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.encode(ArgumentMatchers.any(String.class)))
                .thenAnswer(invocation -> {
                    String rawPassword = invocation.getArgument(0);
                    return "encoded_" + rawPassword;
                });

        UserRequest.UpdateDTO updateDTO = UserRequest.UpdateDTO.builder()
                .username("TestUser")
                .password("1234")
                .email("test@test.com")
                .nickName("change")
                .build();

        // When
        userService.update(updateDTO);

        // Then
        Assertions.assertEquals("encoded_1234", user.getPassword());
        Assertions.assertEquals("test@test.com", user.getEmail());
        Assertions.assertEquals("change", user.getNickName());
    }

    @DisplayName("회원 수정 실패 테스트 - 잘못된 유저네임 요청")
    @Test
    void update_Failed_Test_InvalidUsername() {
        // Given
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        UserRequest.UpdateDTO updateDTO = UserRequest.UpdateDTO.builder()
                .username("TestUser")
                .password("1234")
                .email("test@test.com")
                .nickName("change")
                .build();
        // When
        // Then
        Assertions.assertThrows(Exception500.class, () -> userService.update(updateDTO));

        try {
            userService.update(updateDTO);
        } catch (Exception500 exception) {
            Assertions.assertEquals(ErrorMessage.NOT_FOUND_USER_FOR_UPDATE, exception.getMessage());
        }
    }

    @DisplayName("닉네임 중복 체크 성공 테스트 - 닉네임이 중복되지 않음")
    @Test
    void checkNickName_Success_Test() {
        // Given
        Mockito.when(userRepository.findByNickName(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        UserRequest.CheckNickNameDTO checkNickNameDTO = new UserRequest.CheckNickNameDTO("test");

        // When
        // Then
        userService.checkNickName(checkNickNameDTO);
    }

    @DisplayName("닉네임 중복 체크 실패 테스트 - 닉네임이 중복됨")
    @Test
    void checkNickName_Failed_Test() {
        // Given
        User mockUser = User.builder().nickName("duplicateNickName").build();
        Mockito.when(userRepository.findByNickName(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(mockUser));

        UserRequest.CheckNickNameDTO checkNickNameDTO
                = new UserRequest.CheckNickNameDTO("duplicateNickName");

        // When
        // Then
        Assertions.assertThrows(DuplicateNickNameException.class, () ->
                userService.checkNickName(checkNickNameDTO));

        try {
            userService.checkNickName(checkNickNameDTO);
        } catch (DuplicateNickNameException exception) {
            Assertions.assertEquals(ErrorMessage.DUPLICATE_NICKNAME, exception.getMessage());
        }
    }

    @DisplayName("유저네임 중복 체크 성공 테스트 - 유저네임이 중복되지 않음")
    @Test
    void checkUsername_Success_Test() {
        // Given
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.empty());

        UserRequest.CheckUsernameDTO checkUsernameDTO = new UserRequest.CheckUsernameDTO("test");

        // When
        // Then
        userService.checkUsername(checkUsernameDTO);
    }

    @DisplayName("유저네임 중복 체크 실패 테스트 - 유저네임이 중복됨")
    @Test
    void checkUsername_Failed_Test() {
        // Given
        User mockUser = User.builder().username("duplicateUsername").build();
        Mockito.when(userRepository.findByUsername(ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(mockUser));

        UserRequest.CheckUsernameDTO checkUsernameDTO
                = new UserRequest.CheckUsernameDTO("duplicateUsername");

        // When
        // Then
        Assertions.assertThrows(DuplicateUsernameException.class, () ->
                userService.checkUsername(checkUsernameDTO));

        try {
            userService.checkUsername(checkUsernameDTO);
        } catch (DuplicateUsernameException exception) {
            Assertions.assertEquals(ErrorMessage.DUPLICATE_USERNAME, exception.getMessage());
        }
    }
}