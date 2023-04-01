package clinica.service.impl;

import clinica.entities.Odontologo;
import clinica.exceptions.BadRequestException;
import clinica.exceptions.ResourceNotFoundException;
import clinica.model.OdontologoDTO;
import clinica.repository.IOdontologoRepository;
import clinica.service.CRUDService;
import clinica.util.Mapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OdontologoService implements CRUDService<OdontologoDTO> {
  private final IOdontologoRepository odontologoRepository;
  @Autowired
  public OdontologoService(IOdontologoRepository odontologoRepository) {
    this.odontologoRepository = odontologoRepository;
  }
  
  @Override
  public OdontologoDTO guardar(OdontologoDTO odontologoDTO) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    if (odontologoDTO == null) {
      throw new BadRequestException("El odontólogo no puede ser nulo");
    }
    Odontologo odontologo = Mapper.map(odontologoDTO, Odontologo.class);
    return Mapper.map(odontologoRepository.save(odontologo), OdontologoDTO.class);
  }
  
  @Override
  public OdontologoDTO buscar(Integer id) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    if (id == null || id < 1) {
      throw new BadRequestException("El id no puede ser nulo o negativo");
    }
    Odontologo odontologo = odontologoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró el odontólogo con id " + id));
    return Mapper.map(odontologo, OdontologoDTO.class);
  }
  
  public List<OdontologoDTO> buscarPorNombre(String nombre) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    if (nombre.isEmpty() ) {
      throw new BadRequestException("El nombre no puede ser vacio");
    }
    List<Odontologo> odontologos = odontologoRepository.buscar(nombre).orElse(new ArrayList<>());
    if (odontologos.isEmpty()) {
      throw new ResourceNotFoundException("No se encontró el odontólogo con nombre " + nombre);
    }
    return Mapper.mapList(odontologos, OdontologoDTO.class);
  }
  
  @Override
  public OdontologoDTO actualizar(OdontologoDTO odontologoDTO) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    OdontologoDTO odontologoActualizado;
    if (odontologoDTO == null) {
      throw new BadRequestException("No se pudo actualizar el odontologo null");
    }
    if (odontologoDTO.getId() == null || odontologoDTO.getId() < 1) {
      throw new BadRequestException("El id no puede ser nulo o negativo");
    }
    Optional<Odontologo> odontoloEnBD = odontologoRepository.findById(odontologoDTO.getId());
    if (odontoloEnBD.isPresent()) {
      Odontologo odontologo = Mapper.map(odontologoDTO, Odontologo.class);
      odontologoActualizado = Mapper.map(odontologoRepository.save(odontologo), OdontologoDTO.class);
    } else {
      throw new ResourceNotFoundException("No se encontró el odontólogo con id " + odontologoDTO.getId());
    }
    return odontologoActualizado;
  }
  
  @Override
  public void eliminar(Integer id) throws BadRequestException, ResourceNotFoundException {
    if (id == null || id < 1) {
      throw new BadRequestException("El id no puede ser nulo o negativo");
    }
    if (odontologoRepository.findById(id).isPresent()) {
      odontologoRepository.deleteById(id);
    } else {
      throw new ResourceNotFoundException("No se encontró el odontólogo con id " + id);
    }
  }
  
  @Override
  public List<OdontologoDTO> buscarTodos() throws JsonProcessingException {
    List<Odontologo> odontologos = odontologoRepository.findAll();
    return Mapper.mapList(odontologos, OdontologoDTO.class);
  }
}
