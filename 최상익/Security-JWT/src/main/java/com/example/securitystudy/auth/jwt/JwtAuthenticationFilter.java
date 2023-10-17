package com.example.securitystudy.auth.jwt;

import com.example.securitystudy.auth.model.PrincipalDetails;
import com.example.securitystudy.user.entity.User;
import com.example.securitystudy.user.UserProvider;

import com.example.securitystudy.util.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserProvider userProvider;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserProvider userProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userProvider = userProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String headerValue  = request.getHeader("Authorization");

        if (headerValue != null && headerValue.startsWith("Bearer ")) {
            String accessToken = headerValue.substring(7);
            System.out.println("accessToken = " + accessToken);
            System.out.println("jwtTokenProvider.validateAccessToken(accessToken) = " + jwtTokenProvider.validateAccessToken(accessToken));
            if (jwtTokenProvider.validateAccessToken(accessToken)) {

                String userId = jwtTokenProvider.getUseridFromAcs(accessToken);
                System.out.println("userId = " + userId);
                try {
                    User user = userProvider.retrieveById(Long.parseLong(userId));
                    PrincipalDetails principalDetails = new PrincipalDetails(user);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(principalDetails, null,
                                    principalDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (BaseException e) {
                    System.out.println("e = " + e);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("User retrieval failed");

                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}