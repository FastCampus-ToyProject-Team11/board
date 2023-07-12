package com.fastcampus.board.user;

import com.fastcampus.board.__core.errors.ErrorMessage;
import com.fastcampus.board.__core.errors.exception.Exception500;
import com.fastcampus.board.__core.security.JwtTokenProvider;
import com.fastcampus.board.__core.security.PrincipalUserDetail;
import com.fastcampus.board.user.dto.UserRequest;
import com.fastcampus.board.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserResponse.JoinDTO save(UserRequest.JoinDTO joinDTO) throws IllegalArgumentException {
        if (joinDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_TO_JOIN);

        User user = joinDTO.toEntityWithHashPassword(passwordEncoder);

        User persistenceUser = userRepository.save(user);
        return UserResponse.JoinDTO.from(persistenceUser);
    }

    public UserResponse.LoginDTOWithJWT login(UserRequest.LoginDTO loginDTO) {
        if (loginDTO == null) throw new Exception500(ErrorMessage.EMPTY_DATA_TO_LOGIN);

        UsernamePasswordAuthenticationToken token
                = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());

        Authentication authentication = authenticationManager.authenticate(token);
        PrincipalUserDetail principal = (PrincipalUserDetail) authentication.getPrincipal();
        final User user = principal.getUser();

        UserResponse.LoginDTO loginResponse = UserResponse.LoginDTO.from(user);
        String jwt = JwtTokenProvider.create(user);

        return UserResponse.LoginDTOWithJWT.from(loginResponse, jwt);
    }
}
