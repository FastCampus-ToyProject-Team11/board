package com.fastcampus.board.user;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fastcampus.board.__core.errors.ErrorMessage;
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
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
    }

    @DisplayName("회원 가입 통합 테스트 - 성공")
    @Test
    void join_Success_Test() {
        // Given
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

    @DisplayName("회원 가입 통합 테스트 - 실패(비어 있는 DTO)")
    @Test
    void join_Failed_Test_EmptyDTO() {
        // Given
        UserRequest.JoinDTO joinDTO = null;

        // When
        // Then
        Assertions.assertThrows(Exception500.class, () -> userService.save(null));
        try {
            userService.save(null);
        } catch (Exception500 exception) {
            Assertions.assertEquals(ErrorMessage.EMPTY_DATA_FOR_USER_JOIN, exception.getMessage());
        }
    }

    @DisplayName("로그인 통합 테스트 - 성공")
    @Test
    void login_Success_Test() {
        // Given
        UserRequest.JoinDTO joinDTO = UserRequest.JoinDTO.builder()
                .username("testUser")
                .password("1234")
                .email("test@test.com")
                .nickName("testUser")
                .build();
        userService.save(joinDTO);

        UserRequest.LoginDTO loginDTO = new UserRequest.LoginDTO("testUser", "1234");

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
        UserRequest.JoinDTO joinDTO = UserRequest.JoinDTO.builder()
                .username("testUser")
                .password("1234")
                .email("test@test.com")
                .nickName("testUser")
                .build();
        userService.save(joinDTO);

        UserRequest.LoginDTO loginDTO =
                new UserRequest.LoginDTO("InvalidUsername", "1234");

        // When
        // Then
        Assertions.assertThrows(InternalAuthenticationServiceException.class, () -> userService.login(loginDTO));
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
        Assertions.assertThrows(Exception500.class, () -> userService.login(null));
        try {
            userService.login(null);
        } catch (Exception500 exception) {
            Assertions.assertEquals(ErrorMessage.EMPTY_DATA_FOR_USER_LOGIN, exception.getMessage());
        }
    }
}