package pe.todotic.taller_sba.controller;

import com.paypal.core.PayPalHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import pe.todotic.taller_sba.Utils.UrlBuilder;
import pe.todotic.taller_sba.exception.BadRequestException;
import pe.todotic.taller_sba.model.ItemVenta;
import pe.todotic.taller_sba.model.Libro;
import pe.todotic.taller_sba.model.Venta;
import pe.todotic.taller_sba.repo.ItemVentaRepository;
import pe.todotic.taller_sba.repo.LibroRepository;
import pe.todotic.taller_sba.repo.VentaRepository;
import pe.todotic.taller_sba.service.FileSystemStorageService;
import pe.todotic.taller_sba.service.VentaService;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class HomeController extends BaseController {

    private LibroRepository libroRepository;
    private VentaRepository ventaRepository;
    private PayPalHttpClient payPalHttpClient;
    private ItemVentaRepository itemVentaRepository;
    private FileSystemStorageService fileSystemStorageService;
    private VentaService ventaService;

    @Autowired
    public HomeController(LibroRepository libroRepository, VentaRepository ventaRepository, PayPalHttpClient payPalHttpClient, ItemVentaRepository itemVentaRepository, FileSystemStorageService fileSystemStorageService, VentaService ventaService) {
        this.libroRepository = libroRepository;
        this.ventaRepository = ventaRepository;
        this.payPalHttpClient = payPalHttpClient;
        this.itemVentaRepository = itemVentaRepository;
        this.fileSystemStorageService = fileSystemStorageService;
        this.ventaService = ventaService;
    }

    @GetMapping("/ultimos-libros")
    public List<Libro> index(){
        List<Libro> libros = libroRepository.findTop6ByOrderByFechaCreacionDesc();
        libros.forEach(libro -> libro.setUrlPortada(makeUrlBuilder(libro.getRutaPortada())));
        return libros;
    }

    @GetMapping("/libros")
    Page<Libro> getLibros(@PageableDefault(sort = "titulo",size = 10, direction = Sort.Direction.ASC) Pageable pageable){ // pageDefault es recomendable y la ordenacion
        Page<Libro> libros = libroRepository.findAll(pageable);
        libros.forEach(libro -> {
            libro.setUrlPortada(makeUrlBuilder(libro.getRutaPortada()));
        });
        return libros;
    }

    @GetMapping("/libros/{slug}")
    Libro getLibro(@PathVariable String slug){
        Libro libro = libroRepository.findBySlug(slug).orElseThrow(EntityNotFoundException::new);
        libro.setUrlPortada(makeUrlBuilder(libro.getRutaPortada()));
        return  libro;
    }



    @PostMapping("/pago-paypal")
    Map<String, String> crearPagoPaypal(@RequestBody List<Integer> idLibros, @RequestParam String returnUrl) throws IOException {

        String approval= ventaService.crearVentaPaypal(idLibros,returnUrl);

        Map<String,String> result = new HashMap<>();
        result.put("url", approval);
       return result; // retorno url que muestra pantalla de logue y de pago en paypayl

    }

    @GetMapping("/pago-paypal/ejecutar")
    Map<String,Object>  ejecutarPagoPaypal(@RequestParam String token) throws IOException {

        Map<String,Object> result = new HashMap<>();
            Venta venta=ventaService.ejecutarPagoPayPal(token);

            result.put("success",venta!=null);
            result.put("venta",venta);
            return result;

        }
        @GetMapping("/detalles-venta/{idVenta}")
    Venta getVenta(@PathVariable Integer idVenta){
        Venta venta = ventaRepository.findByIdAndEstado(idVenta, Venta.Estado.COMPLETADO)
                .orElseThrow(EntityNotFoundException::new);

        for(ItemVenta item: venta.getItems()){
            Libro libro= item.getLibro();
            libro.setUrlPortada(makeUrlBuilder(libro.getRutaPortada()));
            libro.setUrlArchivo(makeUrlBuilder(libro.getUrlArchivo()));
        }
        return venta;
    }

    @GetMapping("/descargar-archivo/{idItemVenta}")
    Resource descargarArchivoItemVenta(@PathVariable Integer idItemVenta){
        ItemVenta itemVenta = itemVentaRepository.findById(idItemVenta)
                .orElseThrow(EntityNotFoundException::new);

        if(itemVenta.getNumeroDescargasDisponibles()>0){
            itemVenta.consumirDescargasDisponbiles();
            itemVentaRepository.save(itemVenta);
            return fileSystemStorageService.loadAsResource(itemVenta.getLibro().getRutaArchivo());
        }else{
            throw  new BadRequestException("Ya no existen mas descargas disponibles para este item");
        }
    }


//    public static void main(String[] args) {
//        System.out.println("esta es la ues");
//    }

    //psvm

}
