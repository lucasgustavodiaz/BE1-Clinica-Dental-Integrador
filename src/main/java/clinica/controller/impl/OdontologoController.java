package clinica.controller.impl;

import clinica.controller.CRUDController;
import clinica.exceptions.BadRequestException;
import clinica.exceptions.ResourceNotFoundException;
import clinica.model.OdontologoDTO;
import clinica.service.impl.OdontologoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController implements CRUDController<OdontologoDTO> {
  
  @Autowired
  private OdontologoService odontologoService;
  
  @Autowired
  public OdontologoController(OdontologoService odontologoService) {
    this.odontologoService = odontologoService;
  }
  
  @Operation(summary = "Crea un nuevo odontólogo")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @PostMapping
  public ResponseEntity<OdontologoDTO> guardar(@RequestBody OdontologoDTO odontologoDTO) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    if (odontologoDTO.getId() != null) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    return ResponseEntity.ok(odontologoService.guardar(odontologoDTO));
  }
  
  @Operation(summary = "Busca un odontólogo por ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @GetMapping("/{id}")
  public ResponseEntity<OdontologoDTO> buscar(@PathVariable("id") Integer id) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    OdontologoDTO odontologo = odontologoService.buscar(id);
    return ResponseEntity.ok(odontologo);
  }
  
  @Operation(summary = "Buscar un odontólogo por nombre")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @GetMapping(value = "/", params = "nombre")
  public ResponseEntity<List<OdontologoDTO>> buscarPorNombre(@RequestParam String nombre) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    List<OdontologoDTO> odontologo = odontologoService.buscarPorNombre(nombre);
    return ResponseEntity.ok(odontologo);
  }
  
  @Operation(summary = "Actualiza un odontólogo")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @PutMapping
  public ResponseEntity<OdontologoDTO> actualizar(@RequestBody OdontologoDTO odontologoDTO) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    return ResponseEntity.ok(odontologoService.actualizar(odontologoDTO));
  }
  
  @Operation(summary = "Elimina un odontólogo por ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @DeleteMapping("{id}")
  public ResponseEntity<String> eliminar(@PathVariable("id") Integer id) throws BadRequestException, ResourceNotFoundException {
    odontologoService.eliminar(id);
    return ResponseEntity.ok(("Se eliminó el odontólogo con id " + id));
  }
  
  @Operation(summary = "Busca todos los odontólogos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success | OK"),
      @ApiResponse(responseCode = "404", description = "Bad Request", content = @Content) })
  @GetMapping
  public ResponseEntity<Iterable<OdontologoDTO>> buscarTodos() throws JsonProcessingException {
    return ResponseEntity.ok(odontologoService.buscarTodos());
  }
}
