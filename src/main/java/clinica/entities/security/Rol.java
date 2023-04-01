package clinica.entities.security;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Rol {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rol_seq")
  @SequenceGenerator(name = "rol_seq", sequenceName = "rol_seq", allocationSize = 1)
  private Long id;
  
  @Setter
  private String name;
  
  public Rol(String name) {
    this.name = name;
  }
}
