package clinica.service.security;

import clinica.entities.security.Rol;
import clinica.entities.security.Usuario;
import clinica.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
  private final UsuarioService usuarioService;
  
  @Override
  public void run(ApplicationArguments args) throws BadRequestException {
    usuarioService.guardar(
        Usuario.builder()
        .username("admin")
        .email("admin@gmail.com")
        .password("admin")
        .roles(Set.of(new Rol("ADMIN"), new Rol("USER")))
        .build()
    );
    usuarioService.guardar(
        Usuario.builder()
            .username("user")
            .email("user@gmail.com")
            .password("user")
            .roles(Set.of(new Rol("USER")))
            .build()
    );
  }
}
