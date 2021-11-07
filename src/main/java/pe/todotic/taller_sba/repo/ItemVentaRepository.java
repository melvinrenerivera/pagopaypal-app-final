package pe.todotic.taller_sba.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.todotic.taller_sba.model.ItemVenta;
import pe.todotic.taller_sba.model.Venta;

@Repository
public interface ItemVentaRepository extends JpaRepository<ItemVenta,Integer> {
}
