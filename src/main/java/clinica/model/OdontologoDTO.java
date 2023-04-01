package clinica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OdontologoDTO {
  private Integer id;
  private String numeroMatricula;
  private String nombre;
  private String apellido;
  
  public OdontologoDTO(String numeroMatricula, String nombre, String apellido) {
    this.numeroMatricula = numeroMatricula;
    this.nombre = nombre;
    this.apellido = apellido;
  }
  
}
