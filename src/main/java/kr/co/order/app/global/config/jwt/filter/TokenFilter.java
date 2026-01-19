package kr.co.order.app.global.config.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.order.app.domain.user.application.port.in.QueryUserUseCase;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.config.auth.PrincipalDetailsService;
import kr.co.order.app.global.config.jwt.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class TokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final QueryUserUseCase queryUserUseCase;
    private final PrincipalDetailsService detailsService;

    private static final String LOGIN_CHECK_URL = "/login"; // /login으로 들어오는 요청 필터링 제외

    private static final String ADMIN_LOGIN_CHECK_URL = "/admin/login"; // /admin/login으로 들어오는 요청 필터링 제외

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals(LOGIN_CHECK_URL) || request.getRequestURI().equals(ADMIN_LOGIN_CHECK_URL)){ // 로그인 요청 시, 해당 필터를 무시하고 다음 필터로 진행
            log.info("일반(관리자) 로그인 요청");
            filterChain.doFilter(request, response);
            return;
        }

        if(request.getRequestURI().equals("/h2-console")) {
            filterChain.doFilter(request, response);
            return;
        }

        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    // 액세스 토큰 추출 및 유효성 검사
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException{
        jwtTokenProvider.resolveAccessToken(request)
                .filter(jwtTokenProvider::validateToken)
                .ifPresent(accessToken -> jwtTokenProvider.getUsername(accessToken)
                        .ifPresent(username -> queryUserUseCase.findByUsernameForOptional(username)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    // 인증 객체 생성 및 저장
    public void saveAuthentication(User user){
        UserDetails userDetails = detailsService.loadUserByUsername(user.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
