package com.example.securitystudy.config;

import com.example.securitystudy.auth.service.AuthService;
import com.example.securitystudy.auth.service.PrincipalDetailsService;
import com.example.securitystudy.auth.service.PrincipalOAuth2DetailsService;
import com.example.securitystudy.auth.jwt.JwtAuthenticationFilter;
import com.example.securitystudy.auth.jwt.JwtTokenProvider;
import com.example.securitystudy.auth.jwt.config.CustomAccessDeniedHandler;
import com.example.securitystudy.auth.jwt.config.CustomAuthenticationEntryPoint;
import com.example.securitystudy.auth.jwt.config.OAuth2SuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter  {

    private final PrincipalOAuth2DetailsService principalOAuth2DetailsService;
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    // 일반인증을 위함
    private final PrincipalDetailsService principalDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Autowired
    public SecurityConfig(PrincipalOAuth2DetailsService principalOAuth2DetailsService, AuthService authService, JwtTokenProvider jwtTokenProvider, PrincipalDetailsService principalDetailsService, PasswordEncoder passwordEncoder) {
        this.principalOAuth2DetailsService = principalOAuth2DetailsService;
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.principalDetailsService = principalDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**", "/oauth2/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)


                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())

                .and()
                .formLogin()
                .loginProcessingUrl("/signin")

                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(principalOAuth2DetailsService)

                .and()

                .successHandler(new OAuth2SuccessHandler(jwtTokenProvider,authService));

    }
}
