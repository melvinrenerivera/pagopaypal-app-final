package pe.todotic.taller_sba.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.todotic.taller_sba.model.Venta;

import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta,Integer> {

    Optional<Venta> findByIdAndEstado(Integer id, Venta.Estado estado);

    Page<Venta> findVentaByEstado(Venta.Estado estado, Pageable pageable);
}
