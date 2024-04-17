package ait.cohort34.security;

import ait.cohort34.accounting.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
public class AuthorizationConfiguration {

    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/account/register", "/forum/posts/**").permitAll()
                .requestMatchers("/account/user/{login}/role/{role}").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, "/account/user/{login}").access(new WebExpressionAuthorizationManager("#login == authentication.name"))
                .requestMatchers(HttpMethod.DELETE, "/account/user/{login}").access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMINISTRATOR')"))
                .requestMatchers(HttpMethod.PUT, "/account/user/{login}").access(new WebExpressionAuthorizationManager("#login == authentication.name"))
                .requestMatchers(HttpMethod.POST, "/forum/post/{author}").access(new WebExpressionAuthorizationManager("#author == authentication.name"))
                .requestMatchers(HttpMethod.PUT, "/forum/post/{id}/comment/{author}").access(new WebExpressionAuthorizationManager("#author == authentication.name"))
                .anyRequest().authenticated()
        );
        return http.build();
    }
}
