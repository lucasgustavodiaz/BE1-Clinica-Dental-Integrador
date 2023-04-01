package clinica.service;

import clinica.exceptions.BadRequestException;
import clinica.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CRUDService<T> {
  T guardar(T t) throws BadRequestException, ResourceNotFoundException, JsonProcessingException;
  T buscar(Integer id) throws BadRequestException, ResourceNotFoundException, JsonProcessingException;
  T actualizar(T t) throws BadRequestException, ResourceNotFoundException, JsonProcessingException;
  void eliminar(Integer id) throws BadRequestException, ResourceNotFoundException;
  List<T> buscarTodos() throws JsonProcessingException;
}
