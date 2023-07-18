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

        validateUpdateUserInfo(updateDTO);
        updateValidUserInfo(user, updateDTO);
    }

    private void validateUpdateUserInfo(UserRequest.UpdateDTO updateDTO) {
        String password = updateDTO.getPassword();
        if (!password.equals("") && (password.length() < 4 || password.length() > 15))
            throw new Exception500(ErrorMessage.INVALID_PASSWORD);

        String nickName = updateDTO.getNickName();
        if (!nickName.equals("") && (nickName.length() < 2 || nickName.length() > 15))
            throw new Exception500(ErrorMessage.INVALID_NICKNAME);

        String regex = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        String email = updateDTO.getEmail();
        if (!email.equals("") && !email.matches(regex))
            throw new Exception500(ErrorMessage.INVALID_EMAIL);
    }

    private void updateValidUserInfo(User user, UserRequest.UpdateDTO updateDTO) {
        if (!updateDTO.getEmail().equals("")) {
            user.setEmail(updateDTO.getEmail());
        }
        if (!updateDTO.getNickName().equals("")) {
            user.setNickName(updateDTO.getNickName());
        }
        if (!updateDTO.getPassword().equals("")) {
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

    @Transactional(readOnly = true)
    public void checkEmail(UserRequest.CheckEmailDTO checkEmailDTO) {
        if (checkEmailDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_EMAIL);

        Optional<User> userOptional = userRepository.findByEmail(checkEmailDTO.getEmail());

        userOptional.ifPresent(user -> {
            throw new DuplicateUsernameException();
        });
    }
}
