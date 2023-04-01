package clinica.repository;

import clinica.entities.Odontologo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOdontologoRepository extends JpaRepository<Odontologo, Integer> {
  @Query("SELECT o FROM Odontologo o WHERE o.nombre=?1")
  Optional<List<Odontologo>> buscar(String nombre);
}
