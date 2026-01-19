package kr.co.order.app.global.config.auth;

import kr.co.order.app.domain.user.application.port.in.QueryUserUseCase;
import kr.co.order.app.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final QueryUserUseCase queryUserUseCase;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = queryUserUseCase.findByUsernameOrNull(username);
        if(!(user == null)) return new PrincipalDetails(user);
        return null;
    }
}
