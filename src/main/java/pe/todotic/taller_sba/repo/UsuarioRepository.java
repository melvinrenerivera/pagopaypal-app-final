package pe.todotic.taller_sba.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.todotic.taller_sba.model.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    Optional<Usuario> findOneByEmail(String email);

    boolean existsByEmail(String email);

}
