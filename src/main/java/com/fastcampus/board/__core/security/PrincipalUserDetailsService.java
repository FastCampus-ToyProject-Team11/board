package com.fastcampus.board.__core.security;

import com.fastcampus.board.user.User;
import com.fastcampus.board.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("스프링 시큐리티 로그인 서비스 호출 username: " + username);

        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new IllegalArgumentException("회원 정보가 존재하지 않습니다."));

        return new PrincipalUserDetail(user);
    }
}
