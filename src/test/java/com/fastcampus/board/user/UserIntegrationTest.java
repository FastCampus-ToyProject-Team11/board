package com.fastcampus.board.user;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fastcampus.board.__core.errors.ErrorMessage;
import com.fastcampus.board.__core.errors.exception.DuplicateNickNameException;
import com.fastcampus.board.__core.errors.exception.DuplicateUsernameException;
import com.fastcampus.board.__core.errors.exception.Exception500;
import com.fastcampus.board.__core.security.JwtTokenProvider;
import com.fastcampus.board.user.dto.UserRequest;
import com.fastcampus.board.user.dto.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        UserRequest.JoinDTO joinDTO = UserRequest.JoinDTO.builder()
                .username("testUser")
                .password("1234")
                .email("test@test.com")
                .nickName("testUser")
                .build();

        userService.save(joinDTO);
    }

    @DisplayName("회원 가입 통합 테스트 - 성공")
    @Test
    void join_Success_Test() {
        // Given
        UserRequest.JoinDTO joinDTO = UserRequest.JoinDTO.builder()
                .username("joinTest")
                .password("1234")
                .email("join@test.com")
                .nickName("joinTest")
                .build();

        // When
        UserResponse.JoinDTO actual = userService.save(joinDTO);

        // Then
        Assertions.assertEquals("joinTest", actual.getUsername());
        Assertions.assertEquals("joinTest", actual.getNickName());
    }

    @DisplayName("회원 가입 통합 테스트 - 실패(비어 있는 DTO)")
    @Test
    void join_Failed_Test_EmptyDTO() {
        // Given
        UserRequest.JoinDTO joinDTO = null;

        // When
        // Then
        Assertions.assertThrows(Exception500.class, () -> userService.save(joinDTO));
        try {
            userService.save(joinDTO);
        } catch (Exception500 exception) {
            Assertions.assertEquals(ErrorMessage.EMPTY_DATA_FOR_USER_JOIN, exception.getMessage());
        }
    }

    @DisplayName("로그인 통합 테스트 - 성공")
    @Test
    void login_Success_Test() {
        // Given
        UserRequest.LoginDTO loginDTO =
                new UserRequest.LoginDTO("testUser", "1234");

        // When
        UserResponse.LoginDTOWithJWT response = userService.login(loginDTO);

        UserResponse.LoginDTO responseLoginDTO = response.getLoginDTO();
        String responseJwt = response.getJwt();
        String jwt = responseJwt.replace(JwtTokenProvider.TOKEN_PREFIX, "");

        DecodedJWT decodedJWT = JwtTokenProvider.verify(jwt);

        // Then
        Assertions.assertEquals("testUser", responseLoginDTO.getUsername());
        Assertions.assertEquals("testUser", responseLoginDTO.getNickName());

        Assertions.assertEquals("testUser", decodedJWT.getClaim("username").asString());
        Assertions.assertEquals("SESAC", decodedJWT.getClaim("role").asString());
    }

    @DisplayName("로그인 통합 테스트 - 실패(유저네임 혹은 패스워드 틀림)")
    @Test
    void login_Failed_Test_InvalidUsernameOrPassword() {
        // Given
        UserRequest.LoginDTO loginDTO =
                new UserRequest.LoginDTO("InvalidUsername", "1234");

        // When
        // Then
        Assertions.assertThrows(InternalAuthenticationServiceException.class,
                () -> userService.login(loginDTO));
        try {
            userService.login(loginDTO);
        } catch (InternalAuthenticationServiceException exception) {
            Assertions.assertEquals(ErrorMessage.LOGIN_FAILED, exception.getMessage());
        }
    }

    @DisplayName("로그인 통합 테스트 - 실패(비어 있는 DTO)")
    @Test
    void login_Failed_Test_EmptyDTO() {
        // Given
        UserRequest.LoginDTO loginDTO = null;

        // When
        // Then
        Assertions.assertThrows(Exception500.class, () -> userService.login(loginDTO));
        try {
            userService.login(loginDTO);
        } catch (Exception500 exception) {
            Assertions.assertEquals(ErrorMessage.EMPTY_DATA_FOR_USER_LOGIN, exception.getMessage());
        }
    }

    @DisplayName("회원 정보 수정 통합 테스트 - 성공")
    @Test
    void update_Success_Test() {
        // Given
        UserRequest.UpdateDTO updateDTO = UserRequest.UpdateDTO.builder()
                .username("testUser")
                .password("12345")
                .email("change@change.com")
                .nickName("changeName")
                .build();

        // When
        userService.update(updateDTO);
        User actual = userRepository.findByUsername("testUser").get();

        // Then
        Assertions.assertEquals("testUser", actual.getUsername());
        Assertions.assertTrue(passwordEncoder.matches("12345", actual.getPassword()));
        Assertions.assertEquals("change@change.com", actual.getEmail());
        Assertions.assertEquals("changeName", actual.getNickName());
    }

    @DisplayName("회원 정보 수정 통합 테스트 - 실패(잘못된 유저네임)")
    @Test
    void update_Failed_Test_InvalidUsername() {
        // Given
        UserRequest.UpdateDTO updateDTO = UserRequest.UpdateDTO.builder()
                .username("invalidUsername")
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

    @DisplayName("회원 정보 수정 통합 테스트 - 실패(비어 있는 DTO)")
    @Test
    void update_Failed_Test_EmptyDTO() {
        // Given
        UserRequest.UpdateDTO updateDTO = null;

        // When
        // Then
        Assertions.assertThrows(Exception500.class, () -> userService.update(updateDTO));
        try {
            userService.update(updateDTO);
        } catch (Exception500 exception) {
            Assertions.assertEquals(ErrorMessage.EMPTY_DATA_FOR_USER_UPDATE, exception.getMessage());
        }
    }

    @DisplayName("유저네임 중복 체크 통합 테스트 - 성공(유저네임이 중복되지 않음)")
    @Test
    void checkUsername_Success_Test() {
        // Given
        UserRequest.CheckUsernameDTO checkUsernameDTO
                = new UserRequest.CheckUsernameDTO("checkUsername");

        // When
        // Then
        userService.checkUsername(checkUsernameDTO);
    }

    @DisplayName("유저네임 중복 체크 통합 테스트 - 실패(유저네임이 중복됨)")
    @Test
    void checkUsername_Failed_Test_DuplicateUsername() {
        // Given
        UserRequest.CheckUsernameDTO checkUsernameDTO
                = new UserRequest.CheckUsernameDTO("testUser");

        // When
        // Then
        Assertions.assertThrows(DuplicateUsernameException.class,
                () -> userService.checkUsername(checkUsernameDTO));
        try {
            userService.checkUsername(checkUsernameDTO);
        } catch (DuplicateUsernameException exception) {
            Assertions.assertEquals(ErrorMessage.DUPLICATE_USERNAME, exception.getMessage());
        }
    }

    @DisplayName("유저네임 중복 체크 통합 테스트 - 실패(비어 있는 DTO)")
    @Test
    void checkUsername_Failed_Test_EmptyDTO() {
        // Given
        UserRequest.CheckUsernameDTO checkUsernameDTO = null;

        // When
        // Then
        Assertions.assertThrows(Exception500.class, () -> userService.checkUsername(checkUsernameDTO));
        try {
            userService.checkUsername(checkUsernameDTO);
        } catch (Exception500 exception) {
            Assertions.assertEquals(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_USERNAME, exception.getMessage());
        }
    }
    @DisplayName("닉네임 중복 체크 통합 테스트 - 성공(닉네임이 중복되지 않음)")
    @Test
    void checkNickName_Success_Test() {
        // Given
        UserRequest.CheckNickNameDTO checkNickNameDTO
                = new UserRequest.CheckNickNameDTO("checkNickName");

        // When
        // Then
        userService.checkNickName(checkNickNameDTO);
    }

    @DisplayName("닉네임 중복 체크 통합 테스트 - 실패(닉네임이 중복됨)")
    @Test
    void checkNickName_Failed_Test_DuplicateNickName() {
        // Given
        UserRequest.CheckNickNameDTO checkNickNameDTO
                = new UserRequest.CheckNickNameDTO("testUser");

        // When
        // Then
        Assertions.assertThrows(DuplicateNickNameException.class,
                () -> userService.checkNickName(checkNickNameDTO));
        try {
            userService.checkNickName(checkNickNameDTO);
        } catch (DuplicateNickNameException exception) {
            Assertions.assertEquals(ErrorMessage.DUPLICATE_NICKNAME, exception.getMessage());
        }
    }

    @DisplayName("닉네임 중복 체크 통합 테스트 - 실패(비어 있는 DTO)")
    @Test
    void checkNickName_Failed_Test_EmptyDTO() {
        // Given
        UserRequest.CheckNickNameDTO checkNickNameDTO = null;

        // When
        // Then
        Assertions.assertThrows(Exception500.class, () -> userService.checkNickName(checkNickNameDTO));
        try {
            userService.checkNickName(checkNickNameDTO);
        } catch (Exception500 exception) {
            Assertions.assertEquals(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_NICKNAME, exception.getMessage());
        }
    }
}