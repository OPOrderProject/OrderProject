package kr.co.order.app.global.config.login.provider;

import kr.co.order.app.global.config.auth.PrincipalDetails;
import kr.co.order.app.global.config.auth.PrincipalDetailsService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final BCryptPasswordEncoder passwordEncoder;
    private final PrincipalDetailsService detailsService;

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        PrincipalDetails principalDetails = (PrincipalDetails) detailsService.loadUserByUsername(username);

        if(!passwordEncoder.matches(rawPassword, principalDetails.getPassword())){
            throw new AuthenticationServiceException("패스워드 불일치");
        }

        return new UsernamePasswordAuthenticationToken(principalDetails.getUser(), null,
                principalDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
