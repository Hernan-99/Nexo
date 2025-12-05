package Nexo.app.auth.config.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth->auth
                        .requestMatchers("/auth/**").permitAll() // para hacer que auth sea publico y se puedan
                        // registrar - loguear
                        .anyRequest().authenticated() // api privada, requiere token
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}
