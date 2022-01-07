package ch.wisv.payments.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


/**
 * CHConnectConfiguration class.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ConfigurationProperties(prefix = "wisvch.connect")
@Validated
@Profile("!test")
public class ChConnectConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * Groups that are admin in the system.
     */
    @Getter
    @Setter
    private List<String> adminGroups;

    /**
     * List of all the users that are allowed in the beta.
     */
    @Getter
    @Setter
    private List<String> betaUsers;

    /**
     * The configuration of the authentication.
     * @param http
     * @throws Exception
     */
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/login**", "/webjars/**", "/fonts/**", "/css/**", "/actuator/health").permitAll()
                .and()
                .exceptionHandling().defaultAuthenticationEntryPointFor(getRestAuthenticationEntryPoint(), new AntPathRequestMatcher("/api/**"))
                .and()
                .csrf().disable()
                .authorizeRequests()
                .and()
                .authorizeRequests()
                .anyRequest().hasRole("ADMIN")
                .and()
                .httpBasic()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .oauth2Login().userInfoEndpoint().oidcUserService(oidcUserService());
    }

    private AuthenticationEntryPoint getRestAuthenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Configure the CORS response.
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    /**
     * OidcUserService. This decides the right of the logged-in user.
     * @return OAuth2UserService
     */
    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();

        return (userRequest) -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);

            SimpleGrantedAuthority roleAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");
            SimpleGrantedAuthority roleCommittee = new SimpleGrantedAuthority("ROLE_COMMITTEE");
            SimpleGrantedAuthority roleUser = new SimpleGrantedAuthority("ROLE_USER");
            OidcIdToken idToken = oidcUser.getIdToken();

            Collection<String> groups = (Collection<String>) idToken.getClaims().get("ldap_groups");
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(roleUser);
            if (groups.stream().anyMatch(o -> adminGroups.contains(o))) {
                authorities.add(roleAdmin);
            }
            if (groups.stream().anyMatch(group -> !group.equals("users"))) {
                authorities.add(roleCommittee);
            }
            return new DefaultOidcUser(authorities, idToken, oidcUser.getUserInfo());
        };
    }
}
