package pe.todotic.taller_sba.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import pe.todotic.taller_sba.model.ItemVenta;
import pe.todotic.taller_sba.model.Venta;
import pe.todotic.taller_sba.repo.ItemVentaRepository;
import pe.todotic.taller_sba.repo.VentaRepository;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/admin/ventas")
public class AdminVentaController {

    private final VentaRepository ventaRepository;
    private final ItemVentaRepository itemVentaRepository;

    public AdminVentaController(VentaRepository ventaRepository, ItemVentaRepository itemVentaRepository) {
        this.ventaRepository = ventaRepository;
        this.itemVentaRepository = itemVentaRepository;
    }


    @GetMapping
    public Page<Venta> findVentas(@PageableDefault(size = 10,page = 0) Pageable page){
         Page<Venta> content= this.ventaRepository.findVentaByEstado(Venta.Estado.COMPLETADO,page);
        return content;
    }

    @PutMapping("/detalles-venta")
    public ItemVenta updateDetalleVenta(@RequestBody ItemVenta itemVenta){

        ItemVenta itemVentaBase = this.itemVentaRepository.findById(itemVenta.getId())
                .orElseThrow(EntityNotFoundException::new);

        itemVentaBase.setNumeroDescargasDisponibles(6);
        this.itemVentaRepository.save(itemVentaBase);

        return  itemVentaBase;
    }
}
