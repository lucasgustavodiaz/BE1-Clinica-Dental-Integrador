package clinica.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@ToString
@Getter
@Entity
@Table(name = "domicilios")
public class Domicilio {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "domicilio_seq")
  @SequenceGenerator(name = "domicilio_seq", sequenceName = "domicilio_seq", allocationSize = 1)
  private Integer id;
  
  @Setter
  private String calle;
  
  @Setter
  private Integer numero;
  
  @Setter
  private String localidad;
  
  @Setter
  private String provincia;
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Domicilio domicilio = (Domicilio) o;
    return Objects.equals(id, domicilio.id) && Objects.equals(calle, domicilio.calle) && Objects.equals(numero, domicilio.numero) && Objects.equals(localidad, domicilio.localidad) && Objects.equals(provincia, domicilio.provincia);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id, calle, numero, localidad, provincia);
  }
}
