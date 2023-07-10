package com.fastcampus.board.user;

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

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        Mockito.when(userRepository.save(ArgumentMatchers.any(User.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        Mockito.when(passwordEncoder.encode(ArgumentMatchers.any(String.class)))
                .thenAnswer(invocation -> {
                    String rawPassword = invocation.getArgument(0);
                    return "encoded_" + rawPassword;
                });
    }

    @DisplayName("회원 가입 성공 테스트")
    @Test
    void save_Success_Test() {
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
}