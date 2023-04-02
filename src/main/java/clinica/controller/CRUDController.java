package clinica.controller;

import clinica.exceptions.BadRequestException;
import clinica.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface CRUDController<T> {
  ResponseEntity<?> guardar(@RequestBody T t) throws BadRequestException, ResourceNotFoundException, JsonProcessingException;
  ResponseEntity<?> buscar(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException, JsonProcessingException;
  ResponseEntity<?> actualizar(@RequestBody T t) throws BadRequestException, ResourceNotFoundException, JsonProcessingException;
  ResponseEntity<?> eliminar(@PathVariable Integer id) throws BadRequestException, ResourceNotFoundException;
  ResponseEntity<?> buscarTodos() throws JsonProcessingException;
}
