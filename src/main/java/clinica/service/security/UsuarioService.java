package clinica.service.security;

import clinica.entities.security.Rol;
import clinica.entities.security.Usuario;
import clinica.exceptions.BadRequestException;
import clinica.repository.security.IRolRepository;
import clinica.repository.security.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {
  private final IUsuarioRepository usuarioRepository;
  private final IRolRepository rolRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  
  public Usuario guardar(Usuario usuario) throws BadRequestException {
    if (usuario == null)
      throw new BadRequestException("El usuario no puede ser null");
    rolRepository.saveAll(usuario.getRoles());
    usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
    return usuarioRepository.save(usuario);
  }
  
  public List<Usuario> consultarTodos() {
    return usuarioRepository.findAll();
  }
  
  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Usuario> u = usuarioRepository.findByUsername(username);
    if (u.isEmpty())
      throw new UsernameNotFoundException("No existe el usuario con username: " + username);
  
    Usuario usuario = u.get();
    Set<GrantedAuthority> autorizaciones = new HashSet<>();
    for (Rol rol: usuario.getRoles()) {
      autorizaciones.add(new SimpleGrantedAuthority(rol.getName()));
    }
  
    return new User(usuario.getUsername(), usuario.getPassword(), true, true, true, true, autorizaciones);
  }
}
