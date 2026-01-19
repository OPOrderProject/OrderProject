package kr.co.order.app.global.config.login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CustomJsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login";
    private static final String CONTENT_TYPE = "application/json";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";
    private final ObjectMapper objectMapper; // JSON 데이터 Map 형태로 파싱

    public CustomJsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper){
        super(PathPatternRequestMatcher.pathPattern(HttpMethod.POST, DEFAULT_LOGIN_REQUEST_URL)); // /login POST 요청을 받기 위해 설정
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)){
            throw new AuthenticationServiceException("Content Type이 맞지 않습니다. 다시 시도해주세요");
        }

        String requestMessage = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        Map<String, String> requestMap = objectMapper.readValue(requestMessage, Map.class);

        String username = requestMap.get(USERNAME_KEY);
        request.setAttribute("username", username);
        String password = requestMap.get(PASSWORD_KEY);

        // AuthenticationManager가 인증 시 사용할 인증 대상 객체
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        // PrincipalDetailsService loadUserByUsername() 호출하여 PrincipalDetails 생성 후 username과 passsword 일치 여부를 확인
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
