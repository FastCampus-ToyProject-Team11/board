package com.fastcampus.board.__core.security;

import com.fastcampus.board.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class PrincipalUserDetail implements UserDetails {

    private final User user;

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료되지 않았는지를 리턴 (true: 만료되지 않음)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않는지를 리턴 (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호가 만료되지 않았는지를 리턴 (true: 만료되지 않음)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화(사용 가능) 인지를 리턴 (true: 활성화)
    @Override
    public boolean isEnabled() {
        return true;
    }

    // 계정이 가지고 있는 권한 목록을 리턴한다. (권한이 여러개 있을 수 있어서 루프를 돌아야한다.)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() -> "ROLE_" + user.getRole());

        return collectors;
    }
}
