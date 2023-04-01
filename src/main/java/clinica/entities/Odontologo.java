package clinica.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity
@Table(name = "odontologos")
public class Odontologo {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "odontologo_seq")
  @SequenceGenerator(name = "odontologo_seq", sequenceName = "odontologo_seq", allocationSize = 1)
  @Column(name = "odontologo_id")
  private Integer id;
  
  @Setter
  private String numeroMatricula;
  
  @Setter
  private String nombre;
  
  @Setter
  private String apellido;
  
  @OneToMany(mappedBy = "odontologo", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @JsonIgnore
  private Set<Turno> turnos = new HashSet<>();
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Odontologo that = (Odontologo) o;
    return Objects.equals(id, that.id) && Objects.equals(nombre, that.nombre) && Objects.equals(apellido, that.apellido) && Objects.equals(numeroMatricula, that.numeroMatricula) && Objects.equals(turnos, that.turnos);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id, nombre, apellido, numeroMatricula);
  }
}
