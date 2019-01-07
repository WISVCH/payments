package ch.wisv.payments.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * CH Connect Security Configuration
 */
@Configuration
@EnableWebSecurity
@ConfigurationProperties("wisvch.connect")
@Profile("!test")
public class CHConnectSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private Set<String> adminGroups;

    public void setAdminGroups(Set<String> adminGroups) {
        this.adminGroups = adminGroups;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/api/**", "/login**", "/webjars/**", "/fonts/**", "/css/**", "/management/health").permitAll()
                .anyRequest().hasRole("ADMIN")
            .and()
                .oauth2Login().userInfoEndpoint().oidcUserService(oidcUserService());
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        return userRequest -> {
            SimpleGrantedAuthority ROLE_ADMIN = new SimpleGrantedAuthority("ROLE_ADMIN");
            SimpleGrantedAuthority ROLE_USER = new SimpleGrantedAuthority("ROLE_USER");
            OidcIdToken idToken = userRequest.getIdToken();
            Collection<String> groups = (Collection<String>) idToken.getClaims().get("ldap_groups");
            List<SimpleGrantedAuthority> authorities = groups.stream().anyMatch(adminGroups::contains) ? List.of(ROLE_ADMIN, ROLE_USER) : List.of(ROLE_USER);
            return new DefaultOidcUser(authorities, idToken);
        };
    }
}



//
//    /**
//     * Configure the {@link HttpSecurity} object.
//     */
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        //@formatter:off
//        http.
//                addFilterBefore(oidcAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
//                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
//                .and()
//                .authorizeRequests()
//                .antMatchers("/", "/api/**", "/login**", "/webjars/**", "/fonts/**", "/css/**", "/management/health").permitAll()
//                .anyRequest().hasRole("ADMIN")
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .successForwardUrl("/dashboard")
//                .permitAll()
//                .and()
//                .logout()
//                .logoutSuccessUrl("/")
//                .permitAll()
//                .and()
//                .csrf().disable();
//        //@formatter:on
//    }