package com.fastcampus.board.user;

import com.fastcampus.board.__core.errors.ErrorMessage;
import com.fastcampus.board.__core.errors.exception.DuplicateNickNameException;
import com.fastcampus.board.__core.errors.exception.DuplicateUsernameException;
import com.fastcampus.board.__core.errors.exception.Exception500;
import com.fastcampus.board.user.dto.UserRequest;
import com.fastcampus.board.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public UserResponse.JoinDTO save(UserRequest.JoinDTO joinDTO) throws IllegalArgumentException {
        if (joinDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_FOR_USER_JOIN);

        User user = joinDTO.toEntityWithHashPassword(passwordEncoder);

        User persistenceUser = userRepository.save(user);
        return UserResponse.JoinDTO.from(persistenceUser);
    }

    @Transactional
    public void update(UserRequest.UpdateDTO updateDTO) {
        if (updateDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_FOR_USER_UPDATE);

        Optional<User> userOptional = userRepository.findByUsername(updateDTO.getUsername());
        User user = userOptional.orElseThrow(() -> new Exception500(ErrorMessage.NOT_FOUND_USER_FOR_UPDATE));

        updateValidUserInfo(user, updateDTO);
    }

    private void updateValidUserInfo(User user, UserRequest.UpdateDTO updateDTO) {
        if (updateDTO.getEmail() != null) {
            user.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getNickName() != null) {
            user.setNickName(updateDTO.getNickName());
        }
        if (updateDTO.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(updateDTO.getPassword());
            user.setPassword(encodedPassword);
        }
    }

    @Transactional(readOnly = true)
    public void checkNickName(UserRequest.CheckNickNameDTO checkNickNameDTO) {
        if (checkNickNameDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_NICKNAME);

        Optional<User> userOptional = userRepository.findByNickName(checkNickNameDTO.getNickName());

        userOptional.ifPresent(user -> {
            throw new DuplicateNickNameException();
        });
    }

    @Transactional(readOnly = true)
    public void checkUsername(UserRequest.CheckUsernameDTO checkUsernameDTO) {
        if (checkUsernameDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_USERNAME);

        Optional<User> userOptional = userRepository.findByUsername(checkUsernameDTO.getUsername());

        userOptional.ifPresent(user -> {
            throw new DuplicateUsernameException();
        });
    }
}
