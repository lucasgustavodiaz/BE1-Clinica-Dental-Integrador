package clinica.service.impl;

import clinica.exceptions.ResourceNotFoundException;
import clinica.model.DomicilioDTO;
import clinica.model.OdontologoDTO;
import clinica.model.PacienteDTO;
import clinica.model.TurnoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TurnoServiceTest {
  @Autowired
  private PacienteService pacienteService;
  private PacienteDTO paciente;
  
  @Autowired
  private OdontologoService odontologoService;
  private OdontologoDTO odontologo;
  
  @Autowired
  private TurnoService turnoService;
  
  @BeforeEach
  public void setUp() {
    DomicilioDTO domicilio = new DomicilioDTO();
    domicilio.setCalle("Teodoro Fels");
    domicilio.setNumero(785);
    domicilio.setLocalidad("Springfield");
    domicilio.setProvincia("Springfield");
    
    paciente = new PacienteDTO();
    paciente.setNombre("Lucas");
    paciente.setApellido("Diaz");
    paciente.setCedula("12345678");
    paciente.setDomicilio(domicilio);
    
    odontologo = new OdontologoDTO();
    odontologo.setNombre("Martin");
    odontologo.setApellido("Lobos");
    odontologo.setNumeroMatricula("M008");
  }
  
  @Test
  void guardar() throws Exception {
    PacienteDTO pacienteGuardado = pacienteService.guardar(paciente);
    OdontologoDTO odontologoGuardado = odontologoService.guardar(odontologo);
    TurnoDTO turno = new TurnoDTO();
    turno.setFecha(LocalDateTime.now());
    turno.setPaciente(pacienteGuardado);
    turno.setOdontologo(odontologoGuardado);
    TurnoDTO turnoGuardado = turnoService.guardar(turno);
    assertNotNull(turnoService.buscar(turnoGuardado.getId()));
  }
  
  @Test
  void buscar() throws Exception {
    PacienteDTO pacienteGuardado = pacienteService.guardar(paciente);
    OdontologoDTO odontologoGuardado = odontologoService.guardar(odontologo);
    TurnoDTO turno = new TurnoDTO();
    turno.setFecha(LocalDateTime.of(2023, 3, 26, 16, 30));
    turno.setPaciente(pacienteGuardado);
    turno.setOdontologo(odontologoGuardado);
    TurnoDTO turnoGuardado = turnoService.guardar(turno);
    TurnoDTO turnoBuscado = turnoService.buscar(turnoGuardado.getId());
    assertEquals(turnoGuardado, turnoBuscado);
  }
  
  @Test
  void actualizar() throws Exception {
    TurnoDTO turno = new TurnoDTO();
    turno.setFecha(LocalDateTime.of(2023, 3, 26, 16, 30));
    turno.setPaciente(pacienteService.guardar(paciente));
    turno.setOdontologo(odontologoService.guardar(odontologo));
    TurnoDTO turnoGuardado = turnoService.guardar(turno);
    turnoGuardado.setFecha(LocalDateTime.of(2023, 3, 26, 18, 30));
    turnoService.actualizar(turnoGuardado);
    TurnoDTO turnoBuscado = turnoService.buscar(turnoGuardado.getId());
    assertEquals(turnoGuardado, turnoBuscado);
  }
  
  @Test
  void eliminar() throws Exception {
    TurnoDTO turno = new TurnoDTO();
    turno.setFecha(LocalDateTime.of(2023, 3, 26, 16, 30));
    turno.setPaciente(pacienteService.guardar(paciente));
    turno.setOdontologo(odontologoService.guardar(odontologo));
    TurnoDTO turnoGuardado = turnoService.guardar(turno);
    turnoService.eliminar(turnoGuardado.getId());
    assertThrows(ResourceNotFoundException.class, () -> turnoService.buscar(turnoGuardado.getId()));
  }
  
  @Test
  void buscarTodos() throws Exception {
    TurnoDTO turno = new TurnoDTO();
    turno.setFecha(LocalDateTime.of(2023, 3, 26, 16, 30));
    turno.setPaciente(pacienteService.guardar(paciente));
    turno.setOdontologo(odontologoService.guardar(odontologo));
    turnoService.guardar(turno);
    assertNotEquals(0, turnoService.buscarTodos().size());
  }
}