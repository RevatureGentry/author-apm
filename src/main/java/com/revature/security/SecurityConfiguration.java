package com.revature.security;

import com.revature.security.props.CorsConfigurationProps;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author William Gentry
 */
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final TokenPresentFilter tokenPresentFilter;
  private final CorsConfigurationProps corsConfigurationProps;

  public SecurityConfiguration(TokenPresentFilter tokenPresentFilter, CorsConfigurationProps corsConfigurationProps) {
    this.tokenPresentFilter = tokenPresentFilter;
    this.corsConfigurationProps = corsConfigurationProps;
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web
      .ignoring()
        .mvcMatchers(HttpMethod.GET, "/actuator/*");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .cors(spec -> {
          CorsConfiguration config = new CorsConfiguration();
          config.setAllowedOrigins(corsConfigurationProps.getAllowedOrigins());
          config.setAllowedMethods(corsConfigurationProps.getAllowedMethods());
          config.setAllowedHeaders(corsConfigurationProps.getAllowedHeaders());
          config.setExposedHeaders(corsConfigurationProps.getExposedHeaders());
          config.setAllowCredentials(corsConfigurationProps.isAllowCredentials());
          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("/**", config);
          spec.configurationSource(source);
        })
        .csrf().disable()
        .httpBasic().disable()
        .formLogin().disable()
        .authorizeRequests()
          .anyRequest()
            .authenticated()
          .and()
        .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
        .addFilterAt(tokenPresentFilter, UsernamePasswordAuthenticationFilter.class);
  }
}
