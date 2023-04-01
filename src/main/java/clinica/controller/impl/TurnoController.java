package clinica.controller.impl;

import clinica.controller.CRUDController;
import clinica.exceptions.BadRequestException;
import clinica.exceptions.ResourceNotFoundException;
import clinica.model.TurnoDTO;
import clinica.service.impl.TurnoService;
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
@RequestMapping("/turnos")
public class TurnoController implements CRUDController<TurnoDTO> {
  
  @Autowired
  private TurnoService turnoService;
  
  @Autowired
  public TurnoController(TurnoService turnoService) {
    this.turnoService = turnoService;
  }
  
  @Operation(summary = "Crea un nuevo turno")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @PostMapping
  public ResponseEntity<TurnoDTO> guardar(@RequestBody TurnoDTO turnoDTO) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    if (turnoDTO.getId() != null) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    return ResponseEntity.ok(turnoService.guardar(turnoDTO));
  }
  
  @Operation(summary = "Busca un turno por ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @GetMapping("/{id}")
  public ResponseEntity<TurnoDTO> buscar(@PathVariable("id") Integer id) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    TurnoDTO turnoDTO = turnoService.buscar(id);
    return ResponseEntity.ok(turnoDTO);
  }
  
  @Operation(summary = "Actualiza un turno")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @PutMapping
  public ResponseEntity<TurnoDTO> actualizar(@RequestBody TurnoDTO turnoDTO) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    return ResponseEntity.ok(turnoService.actualizar(turnoDTO));
  }
  
  @Operation(summary = "Elimina un turno por ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @DeleteMapping("{id}")
  public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) throws BadRequestException, ResourceNotFoundException {
    turnoService.eliminar(id);
    return ResponseEntity.ok(("Se eliminó el turno con id " + id));
  }
  
  @Operation(summary = "Busca todos los turnos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @GetMapping
  public ResponseEntity<Iterable<TurnoDTO>> buscarTodos() throws JsonProcessingException {
    return ResponseEntity.ok(turnoService.buscarTodos());
  }
  
}
