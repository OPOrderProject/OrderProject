package kr.co.order.app.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.order.app.domain.user.application.port.in.QueryUserUseCase;
import kr.co.order.app.domain.user.application.port.in.UpdateUserUseCase;
import kr.co.order.app.global.config.auth.PrincipalDetailsService;
import kr.co.order.app.global.config.jwt.filter.TokenFilter;
import kr.co.order.app.global.config.jwt.provider.JwtTokenProvider;
import kr.co.order.app.global.config.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import kr.co.order.app.global.config.login.handler.LoginFailureHandler;
import kr.co.order.app.global.config.login.handler.LoginSuccessHandler;
import kr.co.order.app.global.config.login.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfig {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PrincipalDetailsService principalDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UpdateUserUseCase updateUserUseCase;
    private final QueryUserUseCase queryUserUseCase;
    private final ObjectMapper objectMapper;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/v3/api-docs", "/swagger-resources/**",
                        "/swagger-ui.html", "/webjars/**", "/swagger/**", "/sign-api/exception",
                        "/static/**", "/favicon.ico", "/swagger-ui/index.html");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.formLogin((formLogin) -> formLogin.disable());
        http.httpBasic((httpBasic) -> httpBasic.disable());
        http.csrf((csrf) -> csrf.disable()); // dev 시, 비활성화
        http.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()));

        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests((authorizeRequests) ->
                authorizeRequests
                        .requestMatchers("/login",
                                "/api/v1/user/save",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/swagger/**",
                                "/resources/templates/**",
                                "/api/v1/restaurant/find-all").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated());

        // 자체 필터 시큐리티 필터에 추가
        http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(tokenFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(principalDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(provider);
    }

    @Bean AuthenticationManager customAuthenticationManager(){
        return new ProviderManager(customAuthenticationProvider);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler(){
        return new LoginSuccessHandler(jwtTokenProvider, updateUserUseCase, queryUserUseCase);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler(){
        return new LoginFailureHandler();
    }

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter(){
        CustomJsonUsernamePasswordAuthenticationFilter filter = new CustomJsonUsernamePasswordAuthenticationFilter(
                objectMapper);
        filter.setAuthenticationManager(customAuthenticationManager());
        filter.setAuthenticationSuccessHandler(loginSuccessHandler());
        filter.setAuthenticationFailureHandler(loginFailureHandler());
        return filter;
    }

    @Bean
    public TokenFilter tokenFilter(){
        return new TokenFilter(jwtTokenProvider, queryUserUseCase, principalDetailsService);
    }
}
