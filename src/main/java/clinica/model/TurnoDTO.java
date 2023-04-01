package clinica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TurnoDTO {
  private Integer id;
  private LocalDateTime fecha;
  private PacienteDTO paciente;
  private OdontologoDTO odontologo;
  
  public TurnoDTO(LocalDateTime fecha, PacienteDTO paciente, OdontologoDTO odontologo) {
    this.fecha = fecha;
    this.paciente = paciente;
    this.odontologo = odontologo;
  }
  
}
