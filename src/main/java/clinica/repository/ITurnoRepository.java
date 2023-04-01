package clinica.repository;

import clinica.entities.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITurnoRepository extends JpaRepository<Turno, Integer> {
}
