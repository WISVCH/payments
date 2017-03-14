package ch.wisv.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${payments.admin.username}")
  String username;
  @Value("${payments.admin.password}")
  String password;

  /**
   * Configure the {@link HttpSecurity} object.
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //@formatter:off
      http.
          authorizeRequests()
          .antMatchers("/api/**").permitAll()
          .antMatchers("/", "/login**", "/webjars/**", "/fonts/**", "/css/**").permitAll()
          .anyRequest().hasRole("ADMIN")
      .and()
          .formLogin()
              .loginPage("/login")
              .successForwardUrl("/dashboard")
              .permitAll()
      .and()
          .logout()
              .permitAll()
      .and()
          .csrf().disable();
    //@formatter:on
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .inMemoryAuthentication()
        .withUser(username).password(password).roles("ADMIN");
  }

}