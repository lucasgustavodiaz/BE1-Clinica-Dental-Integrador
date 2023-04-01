package clinica.controller.impl;

import clinica.controller.CRUDController;
import clinica.exceptions.BadRequestException;
import clinica.exceptions.ResourceNotFoundException;
import clinica.model.PacienteDTO;
import clinica.service.impl.PacienteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pacientes")
public class PacienteController implements CRUDController<PacienteDTO> {
  
  @Autowired
  private PacienteService pacienteService;
  
  @Autowired
  public PacienteController(PacienteService pacienteService) {
    this.pacienteService = pacienteService;
  }
  
  @Operation(summary = "Crea un nuevo paciente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @PostMapping
  public ResponseEntity<PacienteDTO> guardar(@RequestBody PacienteDTO pacienteDTO) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    if (pacienteDTO.getId() != null) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    return ResponseEntity.ok(pacienteService.guardar(pacienteDTO));
  }
  
  @Operation(summary = "Busca un paciente por ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @GetMapping("/{id}")
  public ResponseEntity<PacienteDTO> buscar(@PathVariable("id") Integer id) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    PacienteDTO pacienteDTO = pacienteService.buscar(id);
    return ResponseEntity.ok(pacienteDTO);
  }
  
  @Operation(summary = "Actualiza un paciente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @PutMapping
  public ResponseEntity<PacienteDTO> actualizar(@RequestBody PacienteDTO pacienteDTO) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    return ResponseEntity.ok(pacienteService.actualizar(pacienteDTO));
  }
  
  @Operation(summary = "Elimina un paciente por ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @DeleteMapping("{id}")
  public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) throws BadRequestException, ResourceNotFoundException {
    pacienteService.eliminar(id);
    return ResponseEntity.ok(("Se eliminó el paciente con id " + id));
  }
  
  @Operation(summary = "Busca todos los odontólogos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @GetMapping
  public ResponseEntity<Iterable<PacienteDTO>> buscarTodos() throws JsonProcessingException {
    return ResponseEntity.ok(pacienteService.buscarTodos());
  }
}
