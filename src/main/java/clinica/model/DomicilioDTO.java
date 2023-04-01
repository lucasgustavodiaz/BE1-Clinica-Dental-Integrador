package clinica.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DomicilioDTO {
  private Integer id;
  private String calle;
  private Integer numero;
  private String localidad;
  private String provincia;
  
  public DomicilioDTO(String calle, Integer numero, String localidad, String provincia) {
    this.calle = calle;
    this.numero = numero;
    this.localidad = localidad;
    this.provincia = provincia;
  }
  
}
