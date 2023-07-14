package com.fastcampus.board.user;

import com.fastcampus.board.__core.errors.ErrorMessage;
import com.fastcampus.board.__core.errors.exception.Exception500;
import com.fastcampus.board.user.dto.UserRequest;
import com.fastcampus.board.user.dto.UserResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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

}
