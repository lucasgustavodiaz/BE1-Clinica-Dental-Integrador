package clinica.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
public class PacienteDTO {
  private Integer id;
  private String nombre;
  private String apellido;
  private String cedula;
  private LocalDate fechaDeIngreso;
  private DomicilioDTO domicilio;
  
  public PacienteDTO(Integer id, String nombre, String apellido, String cedula, LocalDate fechaDeIngreso) {
    this.id = id;
    this.nombre = nombre;
    this.apellido = apellido;
    this.cedula = cedula;
    this.fechaDeIngreso = fechaDeIngreso;
  }
  
  public PacienteDTO(String nombre, String apellido, String cedula, LocalDate fechaDeIngreso) {
    this.nombre = nombre;
    this.apellido = apellido;
    this.cedula = cedula;
    this.fechaDeIngreso = fechaDeIngreso;
  }
  
}
