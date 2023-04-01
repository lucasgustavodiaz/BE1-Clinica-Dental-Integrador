package clinica.entities.security;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
  @SequenceGenerator(name = "usuario_seq", sequenceName = "usuario_seq", allocationSize = 1)
  private Integer id;
  
  @Setter
  private String username;
  
  @Setter
  private String email;
  
  @Setter
  private String password;
  
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name="UsuarioRoles",
      joinColumns = @JoinColumn(name="id_usuario"),
      inverseJoinColumns = @JoinColumn(name="id_rol")
  )
  private Set<Rol> roles;

}
