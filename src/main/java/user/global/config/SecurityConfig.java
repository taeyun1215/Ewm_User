package user.global.config;

import user.global.OAuth.CustomOAuth2UserService;
import user.global.argumentResolver.LoginArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import user.adapter.out.persistence.UserJpaRepo;
import user.adapter.out.persistence.UserPersistenceMapper;

import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    // sns login
    private final CustomOAuth2UserService customOAuth2UserService;

    private final UserJpaRepo userJpaRepo;
    private final UserPersistenceMapper userPersistenceMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // csrf 보안 설정 끄기
        // 토큰 방식, 즉 stateless 기반 인증에선 서버에서 인증 정보를 보관하지 않기 때문에
        // csrf 공격에 안전하고 매번 csrf 토큰을 받기 않기 때문에 불필요)
        http.csrf().disable();

        // 스프링 시큐리티가 세션을 생성하지 않고 기존 세션을 사용하지도 않음(JWT 사용을 위함)
        http
                .authorizeRequests().antMatchers("/").permitAll()
            .and() /* OAuth */
                .oauth2Login()
                .userInfoEndpoint() // OAuth2 로그인 성공 후 가져올 설정들
                .userService(customOAuth2UserService); // 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시

        return http.build();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginArgumentResolver(userJpaRepo, userPersistenceMapper));
    }

}
