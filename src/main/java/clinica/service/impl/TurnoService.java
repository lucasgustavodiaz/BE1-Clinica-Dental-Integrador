package clinica.service.impl;

import clinica.entities.Turno;
import clinica.exceptions.BadRequestException;
import clinica.exceptions.ResourceNotFoundException;
import clinica.model.OdontologoDTO;
import clinica.model.PacienteDTO;
import clinica.model.TurnoDTO;
import clinica.repository.ITurnoRepository;
import clinica.service.CRUDService;
import clinica.util.Mapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoService implements CRUDService<TurnoDTO> {
  @Autowired
  private CRUDService<PacienteDTO> pacienteService;
  @Autowired
  private CRUDService<OdontologoDTO> odontologoService;
  private final ITurnoRepository turnoRepository;
  
  @Autowired
  public TurnoService(ITurnoRepository turnoRepository) {
    this.turnoRepository = turnoRepository;
  }
  
  @Override
  public TurnoDTO guardar(TurnoDTO turnoDTO) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    if (turnoDTO.getPaciente() == null || turnoDTO.getOdontologo() == null) {
      throw new BadRequestException("El paciente y el odontólogo no pueden ser nulos");
    }
    if (pacienteService.buscar(turnoDTO.getPaciente().getId()) != null
        && odontologoService.buscar(turnoDTO.getOdontologo().getId()) != null) {
      if (sePuedeSacarTurno(turnoDTO)) {
        Turno turno = Mapper.map(turnoDTO, Turno.class);
        turnoDTO = Mapper.map(turnoRepository.save(turno), TurnoDTO.class);
        turnoDTO.setPaciente(pacienteService.buscar(turnoDTO.getPaciente().getId()));
        turnoDTO.setOdontologo(odontologoService.buscar(turnoDTO.getOdontologo().getId()));
      } else {
        throw new BadRequestException("El el odontólogo ya tienen un turno en ese horario");
      }
    } else {
      throw new ResourceNotFoundException("No se encontró el paciente o el odontólogo");
    }
    return turnoDTO;
  }
  
  @Override
  public TurnoDTO buscar(Integer id) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    if (id == null || id < 1) {
      throw new BadRequestException("El id no puede ser nulo o negativo");
    }
    Turno turno = turnoRepository.findById(id).orElseThrow(() ->
        new ResourceNotFoundException("No se encontró el turno con id " + id));
    TurnoDTO turnoDTO = Mapper.map(turno, TurnoDTO.class);
    turnoDTO.setPaciente(pacienteService.buscar(turnoDTO.getPaciente().getId()));
    turnoDTO.setOdontologo(odontologoService.buscar(turnoDTO.getOdontologo().getId()));
    return turnoDTO;
  }
  
  @Override
  public TurnoDTO actualizar(TurnoDTO turnoDTO) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    TurnoDTO turnoActualizado;
    if (turnoDTO == null) {
      throw new BadRequestException("No se pudo actualizar el turno null");
    }
    if (turnoDTO.getId() == null || turnoDTO.getId() < 1) {
      throw new BadRequestException("El id del paciente y el odontologo no puede ser nulo o negativo");
    }
    if (turnoDTO.getPaciente() == null || turnoDTO.getOdontologo() == null) {
      throw new BadRequestException("El paciente y el odontólogo no pueden ser nulos");
    }
    if (pacienteService.buscar(turnoDTO.getPaciente().getId()) == null
        || odontologoService.buscar(turnoDTO.getOdontologo().getId()) == null){
      throw new ResourceNotFoundException("No se encontró el paciente o el odontólogo");
    }
      Optional<Turno> turnoEnBD = turnoRepository.findById(turnoDTO.getId());
    if (turnoEnBD.isPresent()) {
      if (sePuedeSacarTurno(turnoDTO)) {
        turnoDTO.setPaciente(pacienteService.buscar(turnoDTO.getPaciente().getId()));
        turnoDTO.setOdontologo(odontologoService.buscar(turnoDTO.getOdontologo().getId()));
        Turno turno = Mapper.map(turnoDTO, Turno.class);
        turnoActualizado = Mapper.map(turnoRepository.save(turno), TurnoDTO.class);
      } else {
        throw new BadRequestException("El el odontólogo ya tienen un turno en ese horario");
      }
    } else {
      throw new ResourceNotFoundException("No se encontró el turno con id " + turnoDTO.getId());
    }
    return turnoActualizado;
  }
  
  @Override
  public void eliminar(Integer id) throws BadRequestException, ResourceNotFoundException {
    if (id == null || id < 1) {
      throw new BadRequestException("El id no puede ser nulo o negativo");
    }
    Turno turno = turnoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No se encontró el turno con id " + id));
    turnoRepository.delete(turno);
  }
  
  @Override
  public List<TurnoDTO> buscarTodos() throws JsonProcessingException {
    List<Turno> turnos = turnoRepository.findAll();
    return Mapper.mapList(turnos, TurnoDTO.class);
  }
  
  private boolean sePuedeSacarTurno(TurnoDTO turnoDto) throws BadRequestException, ResourceNotFoundException, JsonProcessingException {
    OdontologoDTO odontologoDto = odontologoService.buscar(turnoDto.getOdontologo().getId());
    return turnoRepository.findAll()
        .stream()
        .map(turno -> {
          try {
            return Mapper.map(turno, TurnoDTO.class);
          } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
          }
        })
        .noneMatch(t -> t.getOdontologo().equals(odontologoDto) && t.getFecha().equals(turnoDto.getFecha()));
  }
}
