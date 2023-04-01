package clinica.controller.impl;

import clinica.exceptions.BadRequestException;
import clinica.exceptions.GlobalExceptionsHandler;
import clinica.exceptions.ResourceNotFoundException;
import clinica.model.OdontologoDTO;
import clinica.service.impl.OdontologoService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@WebMvcTest(OdontologoController.class)
@AutoConfigureMockMvc(addFilters = false)
class OdontologoControllerTest {
  private MockMvc mockMvc;
  
  @MockBean
  private OdontologoService odontologoService;
  
  @Autowired
  private OdontologoController odontologoController;
  
  private OdontologoDTO odontologo;
  private OdontologoDTO odontologoInexistente;
  private OdontologoDTO odontologoExistente;
  private List<OdontologoDTO> odontologosEncontrados;
  
  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(odontologoController)
        .setControllerAdvice(GlobalExceptionsHandler.class)
        .build();
    odontologo = new OdontologoDTO("M123", "Lucas", "Diaz");
    odontologoInexistente = new OdontologoDTO(999, "NN", "NN", "NN");
    odontologoExistente = new OdontologoDTO(3, "M123", "Lucas", "Diaz");
    odontologosEncontrados = new ArrayList<>();
  }
  
  @Test
  @DisplayName("Guardar Odontólogo: éxito")
  void guardarOdontolotoOk() throws Exception {
    Mockito.when(odontologoService.guardar(odontologo)).thenReturn(odontologoExistente);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/odontologos")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .content(Mapper.mapObjectToJson(odontologo)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
  
    assertEquals(Mapper.mapObjectToJson(odontologoExistente), response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Guardar Odontólogo: conflicto")
  void guardarOdontolotoConflict() throws Exception {
    odontologo.setId(1);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/odontologos")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .content(Mapper.mapObjectToJson(odontologo)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andReturn();
    
    assertTrue(response.getResponse().getContentAsString().isEmpty());
  }
  
  @Test
  @DisplayName("Buscar Odontólogo por id: éxito")
  void buscarPorIdOk() throws Exception {
    Mockito.when(odontologoService.buscar(3)).thenReturn(odontologoExistente);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/odontologos/3")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    
    assertEquals(Mapper.mapObjectToJson(odontologoExistente), response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Buscar Odontólogo por id: error")
  void buscarPorIdError() throws Exception {
    Mockito.when(odontologoService.buscar(999)).thenThrow(new ResourceNotFoundException("El odontólogo no existe"));
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/odontologos/999")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
    assertEquals("El odontólogo no existe", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Buscar Odontólogos por nombre: éxito")
  void buscarPorNombreOk() throws Exception {
    String nombre = "Lucas";
    Mockito.when(odontologoService.buscarPorNombre(nombre)).thenReturn(odontologosEncontrados);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/odontologos/")
            .param("nombre", nombre)
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    
    assertEquals(Mapper.mapObjectToJson(odontologosEncontrados), response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Buscar Odontólogos por nombre: vacio")
  void buscarPorNombreErrorValidacion() throws Exception {
    String nombre = "";
    Mockito.doThrow(new BadRequestException("El nombre no puede ser vacio")).when(odontologoService).buscarPorNombre(nombre);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/odontologos/")
            .param("nombre", nombre)
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andReturn();
    
    assertEquals("El nombre no puede ser vacio", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Buscar Odontólogos por nombre: no encontrado")
  void buscarPorNombreNoEncontrado() throws Exception {
    String nombre = "Pichon";
    Mockito.doThrow(new ResourceNotFoundException("No se encontró el odontólogo con nombre " + nombre)).when(odontologoService).buscarPorNombre(nombre);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/odontologos/")
            .param("nombre", nombre)
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
  
    assertEquals("No se encontró el odontólogo con nombre " + nombre, response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Eliminar Odontólogo: éxito")
  void eliminarOdontologoOk() throws Exception {
    Mockito.doNothing().when(odontologoService).eliminar(1);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/odontologos/1")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    assertEquals("Se eliminó el odontólogo con id 1", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Eliminar Odontólogo: error")
  void eliminarOdontologoError() throws Exception {
    Mockito.doThrow(new ResourceNotFoundException("No se encontró el odontólogo con id 999")).when(odontologoService).eliminar(999);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.delete("/odontologos/999")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
    assertEquals("No se encontró el odontólogo con id 999", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Actualizar Odontólogo: éxito")
  void actualizarOdontologoOk() throws Exception {
    Mockito.when(odontologoService.actualizar(odontologo)).thenReturn(odontologoExistente);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/odontologos")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .content(Mapper.mapObjectToJson(odontologo)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    assertEquals(Mapper.mapObjectToJson(odontologoExistente), response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Actualizar Odontólogo: error")
  void actualizarOdontologoError() throws Exception {
    Mockito.when(odontologoService.actualizar(odontologoInexistente)).thenThrow(new ResourceNotFoundException("El odontólogo no existe"));
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/odontologos")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8")
            .content(Mapper.mapObjectToJson(odontologoInexistente)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andReturn();
    assertEquals("El odontólogo no existe", response.getResponse().getContentAsString());
  }
  
  @Test
  @DisplayName("Buscar todos los Odontólogos: éxito")
  void buscarTodosOk() throws Exception {
    List<OdontologoDTO> listaOdontologos = Arrays.asList(odontologoExistente);
    Mockito.when(odontologoService.buscarTodos()).thenReturn(listaOdontologos);
    MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/odontologos")
            .contentType(MediaType.APPLICATION_JSON)
            .characterEncoding("utf-8"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andReturn();
    
    assertEquals(Mapper.mapObjectToJson(listaOdontologos), response.getResponse().getContentAsString());
  }
}