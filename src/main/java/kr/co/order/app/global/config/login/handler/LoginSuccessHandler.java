package kr.co.order.app.global.config.login.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.order.app.domain.user.application.port.in.QueryUserUseCase;
import kr.co.order.app.domain.user.application.port.in.UpdateUserUseCase;
import kr.co.order.app.domain.user.domain.User;
import kr.co.order.app.global.config.jwt.provider.JwtTokenProvider;
import kr.co.order.app.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final Duration ACCESS_DURATION = Duration.ofMinutes(30);
    private static final Duration REFRESH_DURATION = Duration.ofDays(7);

    private final JwtTokenProvider jwtTokenProvider;
    private final UpdateUserUseCase updateUserUseCase;
    private final QueryUserUseCase queryUserUseCase;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        User user = queryUserUseCase.findByUsername(getUsername(authentication));
        String accessToken = jwtTokenProvider.createToken(user, ACCESS_DURATION);
        String refreshToken = jwtTokenProvider.createToken(user, REFRESH_DURATION);

        jwtTokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        saveRefreshToken(user, refreshToken);
        addRefreshTokenCookie(request, response, refreshToken);

        log.info("로그인 계정 : "+user.getUsername());
        log.info("Access Token : "+accessToken);
        log.info("Refresh Token : "+refreshToken);
    }

    public void saveRefreshToken(User user, String refreshToken){
        updateUserUseCase.updateRefreshToken(user, refreshToken);
    }

    public void addRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken){
        int maxAge = (int) REFRESH_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, maxAge, true);
    }

    public String getUsername(Authentication authentication){
        User principalDetails = (User) authentication.getPrincipal();

        return principalDetails.getUsername();
    }
}
