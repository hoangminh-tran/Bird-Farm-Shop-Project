package com.tttm.birdfarmshop.Config;

import com.tttm.birdfarmshop.Enums.ERole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfigure {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final RestAccessDenyEntryPoint restAccessDenyEntryPoint;
    private final RestUnauthorizedEntryPoint restUnauthorizedEntryPoint;
    private static final String[] WHITE_LIST_URL = {"/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)

                //Set access denied handler
                .exceptionHandling(access -> access.accessDeniedHandler(restAccessDenyEntryPoint))

                //Set unauthorized handler
                .exceptionHandling(access -> access.authenticationEntryPoint(restUnauthorizedEntryPoint))

                .authorizeHttpRequests(
                        auth ->
                            auth.requestMatchers(WHITE_LIST_URL)
                                    .permitAll()

                )

                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers(
                                        antMatcher("/order/**")
                                )
                                .hasAnyRole(ERole.ADMINISTRATOR.name(), ERole.HEALTHCAREPROFESSIONAL.name(), ERole.CUSTOMER.name(), ERole.SHIPPER.name(), ERole.SELLER.name())
                )

                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers(
                                        antMatcher("/email/**"),
                                        antMatcher(HttpMethod.GET,"/typeOfBird/**"),
                                        antMatcher(HttpMethod.GET,"/food/**"),
                                        antMatcher(HttpMethod.GET,"/nest/**"),
                                        antMatcher(HttpMethod.GET,"/bird/**"),
                                        antMatcher(HttpMethod.GET,"/voucher/**")
                                )
                                .permitAll()
                )

                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/admin/**")
                        .hasRole(ERole.ADMINISTRATOR.name())
                )

                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/healthCareProfestional/**")
                        .hasRole(ERole.HEALTHCAREPROFESSIONAL.name())
                )

                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/shipper/**")
                        .hasRole(ERole.SHIPPER.name())
                )

                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers(
                                        antMatcher(HttpMethod.POST, "/typeOfBird/**"),
                                        antMatcher(HttpMethod.POST, "/food/**"),
                                        antMatcher(HttpMethod.POST, "/nest/**"),
                                        antMatcher(HttpMethod.POST, "/bird/**"),
                                        antMatcher(HttpMethod.PUT, "/typeOfBird/**"),
                                        antMatcher(HttpMethod.PUT, "/food/**"),
                                        antMatcher(HttpMethod.PUT, "/nest/**"),
                                        antMatcher(HttpMethod.PUT, "/bird/**")
                                )
                                .hasAnyRole(ERole.ADMINISTRATOR.name(), ERole.HEALTHCAREPROFESSIONAL.name())
                )

                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers(
                                        antMatcher(HttpMethod.POST, "/voucher/**"),
                                        antMatcher(HttpMethod.PUT, "/voucher/**")
                                )
                                .hasAnyRole(ERole.ADMINISTRATOR.name(), ERole.SELLER.name())
                )

                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new SimpleCORSFilter(), WebAsyncManagerIntegrationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return httpSecurity.build();
    }
}