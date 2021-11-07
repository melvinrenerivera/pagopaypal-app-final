package pe.todotic.taller_sba.service;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.orders.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.todotic.taller_sba.model.ItemVenta;
import pe.todotic.taller_sba.model.Libro;
import pe.todotic.taller_sba.model.Usuario;
import pe.todotic.taller_sba.model.Venta;
import pe.todotic.taller_sba.repo.LibroRepository;
import pe.todotic.taller_sba.repo.UsuarioRepository;
import pe.todotic.taller_sba.repo.VentaRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VentaService {
        private final LibroRepository libroRepository;
        private final VentaRepository ventaRepository;
        private  final UsuarioRepository usuarioRepository;
        private final PayPalHttpClient payPalHttpClient;

    public VentaService(LibroRepository libroRepository, VentaRepository ventaRepository, UsuarioRepository usuarioRepository, PayPalHttpClient payPalHttpClient) {
        this.libroRepository = libroRepository;
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.payPalHttpClient = payPalHttpClient;
    }

        @Transactional
        public String crearVentaPaypal(List<Integer> idLibros, String returnUrl) throws IOException {
            //creacion de la venta
            Double  total= 0D;
            String principal = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //como es stayless solo retorna el nombre de usuario el correo electronico, para una arquitectua mvc eso cambia a AppUserDetail

            Usuario usuario= usuarioRepository.findOneByEmail(principal)
                    .orElseThrow(EntityNotFoundException::new);

            List<ItemVenta> items = new ArrayList<>();
            Venta venta = new Venta();

            for(Integer idLibro : idLibros){
                Libro libro = libroRepository.findById(idLibro)
                        .orElseThrow(EntityNotFoundException::new);

                ItemVenta itemVenta = ItemVenta.builder()
                        .venta(venta)
                        .libro(libro)
                        .precio(libro.getPrecio().doubleValue())
                        .numeroDescargasDisponibles(3)
                        .build();

                items.add(itemVenta);
                total = total + itemVenta.getPrecio();
            }


            venta.setItems(items);
            venta.setFecha(LocalDateTime.now());
            venta.setTotal(new BigDecimal(total));
            venta.setEstado(Venta.Estado.CREADO);
            venta.setUsuario(usuario);

            ventaRepository.save(venta);

            //creacion de la solicitud a la api de paypal

            //creaciond el context para los datos generales de la operacion
            ApplicationContext applicationContext = new ApplicationContext();
            applicationContext.brandName("Mi tienda Online");
            applicationContext.landingPage("NO_PREFERENCE");
            applicationContext.userAction("PAY_NOW"); //boton que aparece
            applicationContext.returnUrl(returnUrl);  //url de retorno
            applicationContext.cancelUrl(returnUrl);  //metodo cancel

            //creacion de la solicitud del pedido

            OrderRequest orderRequest = new OrderRequest();
            orderRequest.checkoutPaymentIntent("CAPTURE");
            orderRequest.applicationContext(applicationContext);

            //creacion de los detalldes del pedido (opcional)
            List<Item> paypayItems = new ArrayList<>();
            venta.getItems().forEach(itemVenta -> {

                Money money = new Money()
                        .currencyCode("USD")
                        .value(itemVenta.getPrecio().toString());

                Item paypayItem = new Item()
                        .name(itemVenta.getLibro().getTitulo())
                        .quantity("1")
                        .unitAmount(money);

                paypayItems.add(paypayItem);

            });

            //creacion del resumen del grupo de items obligatorio
            List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
            //monto total de la venta
            AmountWithBreakdown amountWithBreakdown = new AmountWithBreakdown()
                    .currencyCode("USD")
                    .value(venta.getTotal().toString());


            //Establecer el monto total de la suma de los items
            amountWithBreakdown
                    .amountBreakdown(
                            new AmountBreakdown()
                                    .itemTotal(new Money()
                                            .currencyCode("USD")
                                            .value(venta.getTotal().toString())));


            PurchaseUnitRequest purchaseUnit = new PurchaseUnitRequest()
                    .amountWithBreakdown(amountWithBreakdown)
                    .referenceId(venta.getId().toString())
                    .description("Venta #"+venta.getId())
                    .items(paypayItems);

            purchaseUnits.add(purchaseUnit);
            orderRequest.purchaseUnits(purchaseUnits);
            //creacion de la solicitud de la creacion de la compra
            OrdersCreateRequest request = new OrdersCreateRequest()
                    .requestBody(orderRequest);
            HttpResponse<Order> response = payPalHttpClient.execute(request);
            Order order = response.result();

            String approval = order.links().stream()
                    .filter(link -> link.rel().equals("approve"))
                    .findFirst()
                    .orElseThrow(RuntimeException::new)
                    .href();

            return  approval;
        }


        public Venta ejecutarPagoPayPal(String token) throws IOException {
            OrdersCaptureRequest request = new OrdersCaptureRequest(token);
            try {
                HttpResponse<Order> response = payPalHttpClient.execute(request);
                Order order = response.result();

                Venta venta = null;

                //CREATED, APPROVED, COMPLETED, FAILED

                boolean succes = order.status().equals("COMPLETED");
                if(succes){
                    venta = ventaRepository.findById(Integer.parseInt(order.purchaseUnits().get(0).referenceId()))
                            .orElseThrow(EntityNotFoundException::new);
                    venta.setEstado(Venta.Estado.COMPLETADO);
                    ventaRepository.save(venta);
                }

                return venta;
            }catch (HttpException e){
                return null;
            }
        }

}
