package clinica.service.impl;

import clinica.exceptions.ResourceNotFoundException;
import clinica.model.OdontologoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OdontologoServiceTest {
  @Autowired
  private OdontologoService odontologoService;
  private OdontologoDTO odontologo;
  
  
  @BeforeEach
  public void setUp() {
    odontologo = new OdontologoDTO();
    odontologo.setNombre("Lucas");
    odontologo.setApellido("Diaz");
    odontologo.setNumeroMatricula("M007");
  }
  
  @Test
  void guardar() throws Exception {
    OdontologoDTO odontologoGuardado = odontologoService.guardar(odontologo);
    assertNotNull(odontologoService.buscar(odontologoGuardado.getId()));
  }
  
  @Test
  void buscar() throws Exception {
    OdontologoDTO odontologoGuardado = odontologoService.guardar(odontologo);
    OdontologoDTO odontologoBuscado = odontologoService.buscar(odontologoGuardado.getId());
    assertEquals(odontologoGuardado, odontologoBuscado);
  }

  @Test
  void buscarPorNombre() throws Exception {
    OdontologoDTO odontologoGuardado = odontologoService.guardar(odontologo);
    List<OdontologoDTO> odontologosBuscado = odontologoService.buscarPorNombre(odontologoGuardado.getNombre());
    assertNotEquals(0 ,odontologosBuscado.size());
  }
  
  @Test
  void actualizar() throws Exception {
    OdontologoDTO odontologoGuardado = odontologoService.guardar(odontologo);
    odontologoGuardado.setNombre("Martin");
    odontologoGuardado.setApellido("Lobos");
    odontologoGuardado.setNumeroMatricula("M008");
    OdontologoDTO odontologoActualizado = odontologoService.actualizar(odontologoGuardado);
    assertEquals(odontologoGuardado, odontologoActualizado);
  }
  
  @Test
  void eliminar() throws Exception {
    OdontologoDTO odontologoGuardado = odontologoService.guardar(odontologo);
    odontologoService.eliminar(odontologoGuardado.getId());
    assertThrows(ResourceNotFoundException.class, () -> odontologoService.buscar(odontologoGuardado.getId()));
  }
  
  @Test
  void buscarTodos() throws Exception {
    odontologoService.guardar(odontologo);
    assertNotEquals(0, odontologoService.buscarTodos().size());
  }
}