package clinica.config;

import clinica.entities.security.Rol;
import clinica.repository.security.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
  private final IUsuarioRepository usuarioRepository;
  
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // Permitimos recursos estáticos como CSS y JS
            .requestMatchers(PathRequest.toH2Console()).permitAll() // Permitimos acceso a la consola de H2
            .requestMatchers("/", "/login").permitAll()
            .requestMatchers("/odontologos/**", "/pacientes/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        )
        .csrf().disable()
        .headers().frameOptions().disable().and()
        .formLogin(withDefaults())
        .httpBasic(withDefaults())
        .build();
  }
  
  @Bean
  public UserDetailsService userDetailsService() {
    return username -> usuarioRepository.findByUsername(username)
        .map(u -> User.withUsername(u.getUsername())
            .password(u.getPassword())
            .roles(u.getRoles().stream().map(Rol::getName).toArray(String[]::new))
            .build())
        .orElseThrow(() -> new RuntimeException("No existe el usuario con username: " + username));

  }
  
  @Bean
  public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .userDetailsService(userDetailsService())
        .passwordEncoder(passwordEncoder)
        .and()
        .build();
  }
  
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  
}
