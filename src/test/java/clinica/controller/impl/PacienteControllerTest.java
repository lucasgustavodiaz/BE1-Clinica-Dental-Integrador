package clinica.controller.impl;

import clinica.exceptions.GlobalExceptionsHandler;
import clinica.exceptions.ResourceNotFoundException;
import clinica.model.DomicilioDTO;
import clinica.model.PacienteDTO;
import clinica.service.impl.PacienteService;
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
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(PacienteController.class)
@AutoConfigureMockMvc(addFilters = false)
class PacienteControllerTest {
  private MockMvc mockMvc;
  
  @MockBean
  private PacienteService pacienteService;
  
  @Autowired
  private PacienteController pacienteController;
  
  private PacienteDTO paciente;
  private PacienteDTO pacienteInexistente;
  private PacienteDTO pacienteExistente;
  
  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(pacienteController)
        .setControllerAdvice(GlobalExceptionsHandler.class)
        .build();
    DomicilioDTO domicilio = new DomicilioDTO("SAN MARTIN", 859, "CABA", "BUENOS AIRES");
    paciente = new PacienteDTO("DIEGO","MARADONA","123", LocalDate.now());
    paciente.setDomicilio(domicilio);
    pacienteInexistente = new PacienteDTO(999, "NN", "NN", "NN", LocalDate.now());
    pacienteExistente = new PacienteDTO(3, "DIEGO","MARADONA","123", LocalDate.now());
    domicilio.setId(3);
    pacienteExistente.setDomicilio(domicilio);
    pacienteInexistente.setDomicilio(domicilio);
  }
  
  @Test
  @DisplayName("Guardar paciente: éxito")
  void guardarPacienteOk() throws Exception {
    Mockito.when(pacienteService.guardar(paciente)).thenReturn(pacienteExistente);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/pacientes")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .content(Mapper.mapObjectToJson(paciente)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
  
    assertEquals(Mapper.mapObjectToJson(pacienteExistente), response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Guardar Paciente: conflicto")
  void guardarPacienteConflict() throws Exception {
    paciente.setId(1);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/pacientes")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .content(Mapper.mapObjectToJson(paciente)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andReturn();
    
    assertTrue(response.getResponse().getContentAsString().isEmpty());
  }
  
  @Test
  @DisplayName("Buscar Paciente por id: éxito")
  void buscarPorIdOk() throws Exception {
    Mockito.when(pacienteService.buscar(3)).thenReturn(pacienteExistente);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/3")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    
    assertEquals(Mapper.mapObjectToJson(pacienteExistente), response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Buscar Paciente por id: error")
  void buscarPorIdError() throws Exception {
    Mockito.when(pacienteService.buscar(999)).thenThrow(new ResourceNotFoundException("El paciente no existe"));
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/pacientes/999")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
    assertEquals("El paciente no existe", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Eliminar Paciente: éxito")
  void eliminarPacienteOk() throws Exception {
    Mockito.doNothing().when(pacienteService).eliminar(1);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/pacientes/1")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    assertEquals("Se eliminó el paciente con id 1", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Eliminar Paciente: error")
  void eliminarPacienteError() throws Exception {
    Mockito.doThrow(new ResourceNotFoundException("No se encontró el paciente con id 999")).when(pacienteService).eliminar(999);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/pacientes/999")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
    assertEquals("No se encontró el paciente con id 999", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Actualizar Paciente: éxito")
  void actualizarPacienteOk() throws Exception {
    Mockito.when(pacienteService.actualizar(paciente)).thenReturn(pacienteExistente);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/pacientes")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .content(Mapper.mapObjectToJson(paciente)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    assertEquals(Mapper.mapObjectToJson(pacienteExistente), response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Actualizar Odontólogo: error")
  void actualizarPacienteError() throws Exception {
    Mockito.when(pacienteService.actualizar(pacienteInexistente)).thenThrow(new ResourceNotFoundException("El paciente no existe"));
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/pacientes")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .content(Mapper.mapObjectToJson(pacienteInexistente)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
    assertEquals("El paciente no existe", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Buscar todos los Pacientes: éxito")
  void buscarTodosOk() throws Exception {
    List<PacienteDTO> listaPacientes = Arrays.asList(pacienteExistente);
    Mockito.when(pacienteService.buscarTodos()).thenReturn(listaPacientes);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/pacientes")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    
    assertEquals(Mapper.mapObjectToJson(listaPacientes), response.getResponse().getContentAsString());
  }
}