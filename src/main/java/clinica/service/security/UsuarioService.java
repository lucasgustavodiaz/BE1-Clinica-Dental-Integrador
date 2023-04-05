package clinica.service.security;

import clinica.entities.security.Usuario;
import clinica.exceptions.BadRequestException;
import clinica.repository.security.IRolRepository;
import clinica.repository.security.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
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

}
