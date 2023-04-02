package clinica.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name = "turnos")
public class Turno {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "turno_seq")
  @SequenceGenerator(name = "turno_seq", sequenceName = "turno_seq", allocationSize = 1)
  @Column(name = "turno_id")
  private Integer id;
  
  @Setter
  private LocalDateTime fecha;
  
  @Setter
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = "paciente_id", nullable = false)
  private Paciente paciente;
  
  @Setter
  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = "odontologo_id", nullable = false)
  private Odontologo odontologo;
  
  @Override
  public int hashCode() {
    return Objects.hash(id, fecha, paciente, odontologo);
  }
}
