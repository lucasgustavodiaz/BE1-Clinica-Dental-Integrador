package clinica.controller.impl;

import clinica.exceptions.BadRequestException;
import clinica.exceptions.GlobalExceptionsHandler;
import clinica.exceptions.ResourceNotFoundException;
import clinica.model.DomicilioDTO;
import clinica.model.OdontologoDTO;
import clinica.model.PacienteDTO;
import clinica.model.TurnoDTO;
import clinica.service.impl.TurnoService;
import clinica.util.Mapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(TurnoController.class)
@AutoConfigureMockMvc(addFilters = false)
class TurnoControllerTest {
  private MockMvc mockMvc;
  
  @MockBean
  private TurnoService turnoService;
  
  @Autowired
  private TurnoController turnoController;
  
  private OdontologoDTO odontologo;
  private OdontologoDTO odontologoInexistente;
  private OdontologoDTO odontologoExistente;
  private PacienteDTO paciente;
  private PacienteDTO pacienteInexistente;
  private PacienteDTO pacienteExistente;
  private TurnoDTO turno;
  private TurnoDTO turnoVacio;
  private TurnoDTO turnoExistente;
  private TurnoDTO turnoInexistente;
  
  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(turnoController)
        .setControllerAdvice(GlobalExceptionsHandler.class)
        .build();
    odontologo = new OdontologoDTO("M123", "Lucas", "Diaz");
    odontologoInexistente = new OdontologoDTO(999, "NN", "NN", "NN");
    odontologoExistente = new OdontologoDTO(3, "M123", "Lucas", "Diaz");
    
    DomicilioDTO domicilio = new DomicilioDTO("SAN MARTIN", 859, "CABA", "BUENOS AIRES");
    paciente = new PacienteDTO("DIEGO","MARADONA","123", LocalDate.now());
    paciente.setDomicilio(domicilio);
    pacienteInexistente = new PacienteDTO(999, "NN", "NN", "NN", LocalDate.now());
    pacienteExistente = new PacienteDTO(3, "DIEGO","MARADONA","123", LocalDate.now());
    domicilio.setId(3);
    pacienteExistente.setDomicilio(domicilio);
    pacienteInexistente.setDomicilio(domicilio);
    
    turno = new TurnoDTO(LocalDateTime.of(2023, 3, 29, 16, 30), paciente, odontologo);
    turnoVacio = new TurnoDTO(LocalDateTime.of(2023, 3, 29, 16, 30),null, null);
    turnoExistente = new TurnoDTO(2, LocalDateTime.of(2023, 3, 29, 16, 30), pacienteExistente, odontologoExistente);
    turnoInexistente = new TurnoDTO(999, LocalDateTime.of(2023, 3, 29, 16, 30), pacienteExistente, odontologoExistente);
  }
  
  @Test
  @DisplayName("Guardar turno: éxito")
  void guardarTurnoOk() throws Exception {
    Mockito.when(turnoService.guardar(turno)).thenReturn(turnoExistente);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/turnos")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .content(Mapper.mapObjectToJson(turno)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    
    assertEquals(Mapper.mapObjectToJson(turnoExistente), response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Guardar turno: paciente y odontologo vacios")
  void guardarTurnoPacienteOdontologoVacio() throws Exception {
    Mockito.doThrow(new BadRequestException("El id del paciente y el odontologo no puede ser nulo o negativo")).when(turnoService).guardar(turnoVacio);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/turnos")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .content(Mapper.mapObjectToJson(turnoVacio)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();
    
    assertEquals("El id del paciente y el odontologo no puede ser nulo o negativo", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Guardar turno: erro Odontolo ya tiene un turno en ese horario")
  void guardarTurnoOdontologoYaTieneTurno() throws Exception {
    Mockito.doThrow(new BadRequestException("El el odontólogo ya tienen un turno en ese horario")).when(turnoService).guardar(turno);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/turnos")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .content(Mapper.mapObjectToJson(turno)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();
    
    assertEquals("El el odontólogo ya tienen un turno en ese horario", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Buscar turno por id: éxito")
  void buscarPorIdOk() throws Exception {
    Mockito.when(turnoService.buscar(2)).thenReturn(turnoExistente);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/turnos/2")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    
    assertEquals(Mapper.mapObjectToJson(turnoExistente), response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Buscar turno por id: error")
  void buscarPorIdError() throws Exception {
    Mockito.when(turnoService.buscar(999)).thenThrow(new ResourceNotFoundException("No se encontró el turno con id 999"));
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/turnos/999")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
    assertEquals("No se encontró el turno con id 999", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Eliminar turno: éxito")
  void eliminarTurnoOk() throws Exception {
    Mockito.doNothing().when(turnoService).eliminar(1);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/turnos/1")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    assertEquals("Se eliminó el turno con id 1", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Eliminar turno: error")
  void eliminarTurnoError() throws Exception {
    Mockito.doThrow(new ResourceNotFoundException("No se encontró el turno con id 999")).when(turnoService).eliminar(999);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/turnos/999")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
    assertEquals("No se encontró el turno con id 999", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Actualizar Turno: éxito")
  void actualizarTurnoOk() throws Exception {
    Mockito.when(turnoService.actualizar(turno)).thenReturn(turnoExistente);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/turnos")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .content(Mapper.mapObjectToJson(turno)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    assertEquals(Mapper.mapObjectToJson(turnoExistente), response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Actualizar turno: error")
  void actualizarTurnoError() throws Exception {
    Mockito.when(turnoService.actualizar(turnoInexistente)).thenThrow(new ResourceNotFoundException("El turno no existe"));
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/turnos")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .content(Mapper.mapObjectToJson(turnoInexistente)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
    assertEquals("El turno no existe", response.getResponse().getContentAsString());
  }
  @Test
  @DisplayName("Buscar todos los Turnos: éxito")
  void buscarTodosOk() throws Exception {
    List<TurnoDTO> listaTurnos = Arrays.asList(turnoExistente);
    Mockito.when(turnoService.buscarTodos()).thenReturn(listaTurnos);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/turnos")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    
    assertEquals(Mapper.mapObjectToJson(listaTurnos), response.getResponse().getContentAsString());
  }
}