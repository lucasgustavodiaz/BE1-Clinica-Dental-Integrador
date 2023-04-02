package clinica.service.security;

import clinica.entities.security.Usuario;
import clinica.exceptions.BadRequestException;
import clinica.repository.security.IRolRepository;
import clinica.repository.security.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {
  private final IUsuarioRepository usuarioRepository;
  private final IRolRepository rolRepository;
  private final PasswordEncoder passwordEncoder;

  
  public Usuario guardar(Usuario usuario) throws BadRequestException {
    if (usuario == null)
      throw new BadRequestException("El usuario no puede ser null");
    rolRepository.saveAll(usuario.getRoles());
    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
    return usuarioRepository.save(usuario);
  }
  
  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return usuarioRepository.findByUsername(username)
        .map(usuario -> User.builder()
            .username(usuario.getUsername())
            .password(usuario.getPassword())
            .authorities(usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getName()))
                .collect(Collectors.toSet()))
            .build())
        .orElseThrow(() -> new UsernameNotFoundException("No existe el usuario con username: " + username));
  }
}
