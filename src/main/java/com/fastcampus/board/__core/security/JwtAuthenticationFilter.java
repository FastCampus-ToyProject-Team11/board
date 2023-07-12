package com.fastcampus.board.__core.security;


import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fastcampus.board.__core.errors.ErrorMessage;
import com.fastcampus.board.__core.errors.exception.Exception401;
import com.fastcampus.board.__core.util.FilterResponse;
import com.fastcampus.board.user.Role;
import com.fastcampus.board.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String prefixJwt = request.getHeader(JwtTokenProvider.HEADER);

        if (prefixJwt == null) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = prefixJwt.replace(JwtTokenProvider.TOKEN_PREFIX, "");
        try {
            DecodedJWT decodedJWT = JwtTokenProvider.verify(jwt);

            Long id = decodedJWT.getClaim("id").asLong();
            String roleName = decodedJWT.getClaim("role").asString();
            Role role = Role.from(roleName);

            User user = User.builder().id(id).role(role).build();

            PrincipalUserDetail myUserDetails = new PrincipalUserDetail(user);
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            myUserDetails,
                            myUserDetails.getPassword(),
                            myUserDetails.getAuthorities()
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("JWT created: authentication object is creation");
            chain.doFilter(request, response);

        } catch (SignatureVerificationException sve) {
            FilterResponse.unAuthorized(response, new Exception401(ErrorMessage.TOKEN_UN_AUTHORIZED));
        } catch (TokenExpiredException tee) {
            FilterResponse.unAuthorized(response, new Exception401(ErrorMessage.TOKEN_EXPIRED));
        } catch (JWTDecodeException exception) {
            FilterResponse.unAuthorized(response, new Exception401(ErrorMessage.TOKEN_VERIFICATION_FAILED));
        }
    }
}
