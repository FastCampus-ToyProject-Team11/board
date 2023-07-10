package com.fastcampus.board.user;

import com.fastcampus.board.user.dto.UserRequest;
import com.fastcampus.board.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public UserResponse.JoinDTO save(UserRequest.JoinDTO joinDTO) throws IllegalArgumentException {
        if (joinDTO == null) throw new IllegalArgumentException("회원가입 데이터 오류");

        User user = joinDTO.toEntityWithHashPassword(passwordEncoder);

        User persistenceUser = userRepository.save(user);
        return UserResponse.JoinDTO.from(persistenceUser);
    }
}
